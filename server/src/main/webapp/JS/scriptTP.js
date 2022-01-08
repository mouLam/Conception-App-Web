$(document).ready(function() {
    /* Appliquer le hash index par défaut au premier chargement de la page */
    window.location.assign(window.location.origin + "/v3_war/index.html#index");

    let URL = "http://localhost:8080/v3_war"
    let login;
    let tokenWithBearer;
    let token;

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
            url: URL + "/election/resultats",
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
                url: URL + "/election/candidats/noms",
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
                url : URL + "/election/candidats",
                dataType : "json",
                headers : {"Authorization" : `${tokenWithBearer}`}
            }).done((response) => {
                console.log("First : " + response)
                showUserConnectedOptions(true);
                $('#listCands').empty();
                for (let i = 0; i < response.length; i++) {
                    $.ajax({
                        method : "GET",
                        url : URL +`/election/candidats/${i}`,
                        dataType : "json",
                        headers : {"Authorization" : `${tokenWithBearer}`}
                    }).done((response) => {
                        for (let responseKey in response) {
                            let data = response[responseKey];
                            // data = { prenom : "", nom : "" }
                            datas.push(data);
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
                url : URL + "/election/candidats",
                dataType : "json",
                headers : {"Authorization" : `${tokenWithBearer}`}
            }).done((response) => {
                showUserConnectedOptions(true);
                $('#candidat-select').empty();
                for (let i = 0; i < response.length; i++) {
                    $.ajax({
                        method : "GET",
                        url : URL +`/election/candidats/${i}`,
                        dataType : "json",
                        headers : {"Authorization" : `${tokenWithBearer}`}
                    }).done((response) => {
                        for (let responseKey in response) {
                            let data = response[responseKey];
                            candidates.push(data);
                        }
                        templateThis("#vote-template",
                            {voteUserConnected : candidates},
                            "#vote select");
                    });
                }
                $('#vote-form').on('submit', (e) => {
                    e.preventDefault();
                    sendVote();
                });
            });
        }
    }

    function sendVote() {
        console.log("inside send vote function");
        let formData = new FormData();
        formData.append("nomCandidat", $('#candidat-select').val());
        let payload = JSON.stringify(Object.fromEntries(formData)) ;
        console.log(formData);
        $.ajax({
            method : "POST",
            url : URL + "/election/ballots",
            contentType : "application/json",
            dataType : "json",
            headers : {"Authorization" : `${tokenWithBearer}`},
            data : payload,
        }).done(() => {
            window.location.assign(window.location.origin + "/v3_war/index.html#ballot");
        });
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
            console.log(sessionStorage);

            $.ajax({
                method : "POST",
                url : URL + "/users/login",
                contentType : "application/json",
                dataType : "json",
                data : payload,
            }).done(function (response, textStatus, request) {
                //console.log(request.getAllResponseHeaders());
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
                url : URL + "/users/logout",
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
            url: URL + `/users/${login}`,
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
            url: URL + `/users/${login}/nom`,
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
        console.log("Data in template : " + data);
        $(`${idHtmlElement}`).html(rendered);
    }
});

