
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
