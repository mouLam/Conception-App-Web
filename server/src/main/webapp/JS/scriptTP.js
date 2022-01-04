let URL = "http://localhost:8080/v3_war"
let login;
let tokenWithBearer;
let token;


let menuForConnected = ["#monCompte", "#vote", "#ballot", "#deco"];

/**
 * Plier et déplier le menu
 */
$('#menuHeader').click(() => {
    $('#menuUl').slideToggle();
});

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
window.addEventListener('hashchange', (event) => {
    let hash = window.location.hash;
    let link = hash.replace('#', '').toString();
    event.preventDefault();

    if (link === "index") {
        goToIndex();
    } else if (link === "candidats") {
        goToCandidatsNames();
    } else if (link === "connect") {
        connectUser();
    } else if (link === "deco") {
        disconnectUser();
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
            for (let i = 0; i < response.Elections.length; i++) {
                var nomCand = response.Elections[i].nomCandidat;
                var votesCand = response.Elections[i].votes;
                var new_li = $('<li></li>').addClass('candidat_'+i);
                new_li.text(nomCand + " : " + votesCand);
                new_li.appendTo('#cands');
            }
        } else {
            showUserConnectedOptions(true);
            console.log("I have options");
        }
    });
}

function goToCandidatsNames() {
    console.log("In candidats link");
    $.ajax({
        method: "GET",
        url: URL + "/election/candidats/noms",
        dataType : "json"
    }).done((response) => {
        console.log(response);
        $('#listCands').empty();
        if (login === null || login === undefined || login === "") {
            showUserConnectedOptions(false);
            for (var key in response) {
                var new_li = $('<li></li>');
                console.log(response[key]);
                new_li.text(response[key]);
                new_li.appendTo('#listCands');
            }
        }

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