$(document).ready(function() {
    let URL = "https://192.168.75.41/api";
    let URL2 = "https://192.168.75.41/v3_war";
    //let URL = "http://localhost:8080";

    /* Appliquer le hash index par défaut au premier chargement de la page */
    window.location.assign(URL + "/client/index.html#index");

    let login;
    let tokenWithBearer;
    let token;
    let voteId;

    /**
     * Plier et déplier le menu
     */
    $('#menuHeader').click(() => {
        $('#menuUl').slideToggle();
    });

    /**
     *  Edition input du nom de l'utilisateur
     */
    $('input').attr("contentEditable", "true");

    /**
     * Connexion
     */
    $("#loginForm").attr('required', '');
    $("#nomForm").attr('required', '');

    function showUserConnectedOptions(bool) {
        if(bool === false){
            $('#vote_link').hide();
            $('#account_link').hide();
            $('#vote_form_link').hide();
            $('#logout_link').hide();
            $('#login_link').show();
        }else{
            $('#login_link').hide();
            $('#vote_link').show();
            $('#account_link').show();
            $('#vote_form_link').show();
            $('#logout_link').show();
        }
    }

    if (sessionStorage.getItem('status') == null) {
        showUserConnectedOptions(false);
    }

    /**
     * Affichage
     */
    function affichageHash(hash) {
        $('.active')
            .removeClass('active')
            .addClass('inactive');
        $(hash)
            .removeClass('inactive')
            .addClass('active');
    }

    /**
     * Routage
     */
    window.addEventListener('hashchange', () => {
        let hash = window.location.hash;
        let link = hash.replace('#', '').toString();

        if (link === "index") {
            goToIndex();
        } else if (link === "candidats") {
            goToCandidatsNames();
        } else if (link === "connect") {
            connectUser();
        } else if (link === "deco") {
            disconnectUser();
        } else if (link === "monCompte") {
            myAccount();
            $("#changeNameAccount").on('submit', (e) => {
                e.preventDefault();
                changeName();
            });
        } else if (link === "vote") {
            goToVotePage();
        } else if (link === "ballot") {
            goToUserBallot();
        } else if (link === "candidat") {
            getCandidatInfo();
        }

        console.log("Hash : " + hash);
        affichageHash(hash);
    })

    /**
     * Fonctions
     */
    function goToIndex() {
        console.log("In index link");
        $.ajax({
            method: "GET",
            url: URL2 + "/election/resultats",
            dataType : "json"
        }).done((response) => {
            $('#cands').empty();
            if (login === null  || login === undefined || login === "") {
                showUserConnectedOptions(false);
                console.log("I don't have options")
            } else {
                showUserConnectedOptions(true);
                console.log("I have options");
            }
            templateThis("#index-template",
                {results : response.Elections},
                "#index ul");
        });
    }

    function goToCandidatsNames() {
        console.log("In candidats link");
        if (login === null || login === undefined || login === "") {
            $.ajax({
                method: "GET",
                url: URL2 + "/election/candidats/noms",
                dataType : "json"
            }).done((response) => {
                console.log(response);
                $('#listCands').empty();
                showUserConnectedOptions(false);
                templateThis("#candidats-template",
                    {candidatsUserNotConnected : response},
                    "#candidats ul");
            });
        } else {
            let datas = [];
            $.ajax({
                method : "GET",
                url : URL2 + "/election/candidats",
                dataType : "json",
                headers : {"Authorization" : `${tokenWithBearer}`}
            }).done((response) => {
                //console.log("Resp : " + JSON.stringify(response))
                showUserConnectedOptions(true);
                $('#listCands').empty();
                let ids = [];
                for (const idsKey in response) {
                    let resp = response[idsKey].split("/");
                    let id = resp[resp.length - 1];
                    ids.push(id);
                }

                console.log("TAB : " + JSON.stringify(ids))

                for (let i = 0; i < ids.length; i++) {
                    let id = ids[i];
                    $.ajax({
                        method : "GET",
                        url : URL2 +`/election/candidats/${id}`,
                        dataType : "json",
                        headers : {"Authorization" : `${tokenWithBearer}`}
                    }).done((response) => {
                        for (let responseKey in response) {
                            console.log("res2 : ", response);
                            console.log("resKey : ", responseKey);
                            let data = response[responseKey];
                            let infos = {
                                prenom : data["prenom"],
                                nom : data["nom"],
                                id : id
                            }
                            // data = { prenom : "", nom : "" }
                            datas.push(infos);
                        }
                        templateThis("#candidats-template",
                            {candidatsUserConnected : datas},
                            "#candidats ul");
                    });
                }
            });
        }
    }

    function goToVotePage(){
        let candidates = [];
        if (login === null || login === undefined || login === ""){
            window.location.assign(window.location.origin + "/v3_war/index.html#index");
        }else{
            $.ajax({
                method : "GET",
                url : URL2 + "/election/candidats",
                dataType : "json",
                headers : {"Authorization" : `${tokenWithBearer}`}
            }).done((response) => {
                showUserConnectedOptions(true);
                $('#candidat-select').empty();
                for (let i = 0; i < response.length; i++) {
                    $.ajax({
                        method : "GET",
                        url : URL2 +`/election/candidats/${i}`,
                        dataType : "json",
                        headers : {"Authorization" : `${tokenWithBearer}`}
                    }).done((response) => {
                        for (let responseKey in response) {
                            let data = response[responseKey];
                            candidates.push(data);
                        }
                        console.log(candidates);
                        templateThis("#vote-template",
                            {voteUserConnected : candidates},
                            "#candidat-select");
                        console.log("template this");
                    });
                }
                $('#vote-form').on('submit', (e) => {
                    e.preventDefault();
                    sendVote();
                });
            });
        }
    }

    function goToUserBallot(){
        if (login === null || login === undefined || login === ""){
            window.location.assign(window.location.origin + "/v3_war/index.html#index");
        }else {
            console.log("inside ballot function");
            $.ajax({
                method: "GET",
                url: URL2 + `/election/ballots/byUser/${login}`,
                contentType: "application/json",
                headers: {"Authorization": `${tokenWithBearer}`},
            }).done((response) => {
                voteId = response.split("/").at(-1);
                $.ajax({
                    method: "GET",
                    url : URL2 + `/election/votes/${voteId}`,
                    contentType: "application/json",
                    headers: {"Authorization": `${tokenWithBearer}`}
                }).done((response) => {
                    console.log(JSON.stringify(response));
                    templateThis("#ballot-template",
                        {ballotUserConnected : {"nom": response[0], "prenom":response[1]}} ,
                        "#vote-result");
                    window.location.assign(window.location.origin + "/v3_war/index.html#ballot");
                });
            }).fail(() => {
                alert("Vous n'avez pas encore voté");
            });

            $('#delete-vote').on('submit',(e) => {
                e.preventDefault();
                deleteVote(voteId);
            });
        }
    }

    function sendVote() {
        let formData = new FormData();
        formData.append("nomCandidat", $('#candidat-select').val());
        let payload = JSON.stringify(Object.fromEntries(formData)) ;
        $.ajax({
            method : "POST",
            url : URL2 + "/election/ballots",
            contentType : "application/json",
            headers : {"Authorization" : `${tokenWithBearer}`},
            data : payload,
        }).done(() => {
            $('#del-btn').prop('disabled', false);
            goToUserBallot();
        }).fail((error) => {
            console.log(error);
        });
    }

    function deleteVote(id) {
        $.ajax({
            method : "DELETE",
            url : URL2 + `/election/ballots/${id}`,
            contentType : "application/json",
            headers : {"Authorization" : `${tokenWithBearer}`}
        }).done(() => {
            $('#del-btn').prop('disabled', true);
            templateThis("#ballot-template",
                {ballotUserConnectedDelete: "Votre vote a bien été supprimé"},
                "#vote-result");
        }).fail((error) => {
            console.log(error);
            templateThis("ballot-template",
                "Une erreur est survenue, votre vote n'a pas pu être supprimé",
                "#vote-result");
        });
    }

    function getCandidatInfo() {
        const  id = $('#candidats ul li').attr("id")
        $.ajax({
            method : "GET",
            url : URL2 + `/election/candidats/${id}`,
            dataType : "json",
            headers : {"Authorization" : `${tokenWithBearer}`}
        }).done((response) => {
            templateThis("#candidat-template",
                {infoCandidat : response},
                "#candidat div");
        })
    }

    function connectUser() {
        $('#connexion').on('submit',(e) => {
            e.preventDefault();
            let formData = new FormData();
            formData.append("login", $('#loginForm').val());
            formData.append("nom", $('#nomForm').val());
            formData.append("admin", $('#adminForm').is(':checked'));
            let payload = JSON.stringify(Object.fromEntries(formData)) ;

            sessionStorage.setItem('status', 'loggedIn');
            sessionStorage.setItem('login', $('#loginForm').val());
            console.log(sessionStorage);

            $.ajax({
                method : "POST",
                url : URL2 + "/users/login",
                contentType : "application/json",
                dataType : "json",
                data : payload,
            }).done(function (response, textStatus, request) {
                tokenWithBearer = request.getResponseHeader("Authorization");
                token = tokenWithBearer.replace("Bearer ", "");
                login = $('#loginForm').val();
                showUserConnectedOptions(true);
                window.location.assign(window.location.origin + "/v3_war/index.html#index")
            });
        });
    }

    function disconnectUser() {
        $('#deco').on("submit", (e) => {
            e.preventDefault();
            $.ajax({
                method : "POST",
                url : URL2 + "/users/logout",
                contentType : "application/json",
                dataType : "json",
                header : {"Authorization" : `${tokenWithBearer}`}
            }).done( () => {
                window.location.assign(window.location.origin + "/v3_war/index.html#index");
                sessionStorage.setItem("status", null);
                login = null;
                token = null;
                tokenWithBearer = null;
                showUserConnectedOptions(false);
            });
        });
    }

    function myAccount() {
        console.log("In account link");
        $('#errMsg').empty()
        $.ajax({
            method: "GET",
            url: URL2 + `/users/${login}`,
            dataType : "json",
            headers : {"Authorization" : `${tokenWithBearer}`}
        }).done((response) => {
            console.log(response);
            templateThis("#monCompte-template",
                {infoUser : response},
                "#monCompte ul");
        }).fail((error) => {
            $('#errMsg').text("La requête s'est terminée en échec. Infos : "
                + JSON.stringify(error));
        });
    }

    function changeName() {
        console.log("In account changeName link");
        let formData = new FormData();
        let nom = $('#nomChange').val();
        formData.append("nom", nom);
        let payload = JSON.stringify(Object.fromEntries(formData));
        $.ajax({
            method: "PUT",
            url: URL2 + `/users/${login}/nom`,
            data: payload,
            dataType: "json",
            header: {
                "Content-Type": "application/json;charset=UTF-8",
                "Authorization": `${tokenWithBearer}`,
                "Cache-Control" : "no-cache"
            },
            contentType: "application/json"
        }).done((response) => {
            window.location.assign(window.location.origin + "/v3_war/index.html#monCompte");
            $('#nom').empty().text(response["nom"]);
            $('#nomChange').val("");
        }).fail((error) => {
            console.log(error);
        });

    }

    /**
     * Mustache templating
     */
    function templateThis(idScriptToTemplate, data, idHtmlElement) {
        let template = $(idScriptToTemplate).html();
        Mustache.parse(template);
        let rendered = Mustache.render(template, data);
        $(`${idHtmlElement}`).html(rendered);
    }
});

