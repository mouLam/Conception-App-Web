let URL = "http://localhost:8080/v3_war"
let login;
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
$('#connexion').on('submit',(e) => {
    e.preventDefault();
    sessionStorage.setItem('status', 'loggedIn');
    console.log(sessionStorage);
    console.log("form submitted ?");
    $.ajax({
        method : "POST",
        url : URL + "/election/users/login",
        contentType : "application/json",
    }).done((response) => {
        console.log("yes")
        login = null;
        token = null;
        tokenWithBearer = null;
    });
});

if(sessionStorage.getItem('status') == null){
    $('#vote_link').hide();
    $('#account_link').hide();
    $('#vote_form_link').hide();
}else{
    $('#login_link').hide();
    $('#vote_link').show();
    $('#account_link').show();
    $('#vote_form_link').show();
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
        console.log("In index link");
        $.ajax({
            method: "GET",
            url: URL + "/election/resultats",
            dataType : "json"
        })
        .done((response) => {
            //console.log(response);
            //console.log(response.Elections);
            for (let i = 0; i < response.Elections.length; i++) {
                var nomCand = response.Elections[i].nomCandidat;
                var votesCand = response.Elections[i].votes;
                var new_li = $('<li></li>').addClass('candidat_'+i);
                new_li.text(nomCand + " : " + votesCand);
                new_li.appendTo('#cands');
            }
        });
    } else if (link === "candidats") {
        console.log("In conenct link");
        $.ajax({
            method: "GET",
            url: URL + "/election/candidats/noms",
            dataType : "json"
        }).done((response) => {
            console.log(response);
            for (var key in response) {
                var new_li = $('<li></li>');
                console.log(response[key]);
                new_li.text(response[key]);
                new_li.appendTo('#listCands');
            }
        });

    }
    console.log("Hash : " + hash);
    affichageHash(hash);
})
