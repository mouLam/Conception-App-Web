//Bilbliothèque de fonctions AJAX permettant l'envoi de requêtes au serveur de manière asynchrone
//et le traitement (ajout au corpos du document appelant) de réponses en XML conformes à la structure décrite dans l'énoncé du devoir.

//--------------------Fonctions principales---------------------

//fonction principale, qui envoie la  requête au serveur de façon asynchrone et positionne la fonction qui va traiter les données
function loadXMLAsynchroneously(method, request, parameters, id)
{
    //initialisation de l'objet XMLXhttpRequest
    var xhr = initRequete ();

    //spécification de la fonction de traitement à appeler au retour serveur (car chargement asynchrone)
    var XMLDoc = null;
    xhr.onreadystatechange = function() { getXMLDocument(xhr, XMLDoc, id); };

    //envoi de la requête de chargement du fichier XML au serveur
    //le dernier paramètre est true ; le chargement du fichier se fera en asynchrone
    xhr.open(method, request, true);
    //encodage des paramètres dans la requête, si la méthode est post
    if(parameters && (method == "post" || method == "POST"))
        xhr.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
    xhr.send(parameters);
}

//autre fonction principale, plus simple, qui envoie la requête au serveur de façon asynchrone et ne se préoccupe pas de la réponse
//remarque : l'utilisation de cette fonction n'est pas nécessaire pour réaliser le devoir.
function sendRequestAsynchroneously(method, request, parameters)
{
    //initialisation de l'objet XMLXhttpRequest
    var xhr = initRequete ();

    //envoi de la requête de chargement du fichier XML au serveur
    //le dernier paramètre est true ; le chargement du fichier se fera en asynchrone
    xhr.open(method, request, true);
    //encodage des paramètres dans la requête, si la méthode est post
    if(parameters && (method == "post" || method == "POST"))
        xhr.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
    xhr.send(parameters);
}

//--------------------------fonctions secondaires---------------------------

//fonction appelée lors de la réception de la réponse, si la fonction principale loadXMLAsynchroneously() a été utilisée.
function getXMLDocument(xhr, XMLDoc, id)
{
    // teste si la réponse est disponible
    if (xhr.readyState==4) {
        // teste si la réponse est arrivée et contient des données (code HTTP = 200 : OK)
        if (xhr.status == 200) {
            // teste si la réponse est arrivée en XML ou en texte (peut arriver pour certaines configurations d'Apache)
            if (xhr.responseXML != null) {
                XMLDoc= xhr.responseXML;
            } else if (xhr.responseText != null) {
                //si la réponse est en texte, transformation en XML (voir fonction façade plus bas)
                XMLDoc= parseFromString(xhr.responseText);
            }
            //Décommentez la ligne suivante pour voir le contenu XML obtenu (ne marche qu'avec FF)
            //alert((new XMLSerializer()).serializeToString(XMLDoc));

            //appel de la fonction de traitement qui va ajouter les données au corps de la page (à écrire)
            traiteXML (XMLDoc, id);

            //teste si le code de statut est autre que le code renvoyé en cas d'absence de nouveaux messages.
            //Remarque : le code 1223 provient d'un bug avec IE : http://trac.dojotoolkit.org/ticket/2418
        } else if (xhr.status >= 400 && xhr.status != 1223) {
            alert("Un problème est survenu avec la requête : ");
        }
    }
}

//----------------------Fonctions façades----------------------------
//permettent de masquer les différences entre les navigateurs
//remarque : ces fonctions ont uniquement été testées avec FF et IE7

//fonction façade qui teste le type de navigateur et renvoie un objet capable de se charger de l'envoi de la requête.
function initRequete()
{
    var xhr = null;
    if (window.XMLHttpRequest) {
        xhr = new XMLHttpRequest();
    }
    else if (window.ActiveXObject)
    {
        xhr = new ActiveXObject("Microsoft.XMLHTTP");
    }
    return xhr;
}

//fonction façade qui reçoit une chaîne de caractères et la parse en XML pour renvoyer un objet DOM.
//remarque : le XML doit être bien formé, sans quoi l'erreur de parsing bloque l'exécution du script.
function parseFromString (docTXT) {
    // code for IE
    if (window.ActiveXObject)
    {
        var XMLDoc=new ActiveXObject("Microsoft.XMLDOM");
        XMLDoc.async="false";
        XMLDoc.loadXML(docTXT);
    }
    // code for Mozilla, Firefox, Opera, etc.
    else
    {
        var parser=new DOMParser();
        var XMLDoc=parser.parseFromString(docTXT,"text/xml");
    }
    return XMLDoc;
}

//fonction façade qui renvoie le texte contenu dans un élément XML
function getXMLTextContent(source)
{
    if (source.textContent != null) {
        //Gecko
        return source.textContent;
    } else {
        //IE
        return source.text;
    }
}

function traiteXML(XMLDoc, id) {
    const ulElement = document.getElementById(id);

    const name = XMLDoc.getElementsByTagName('h')[0];
    const nameContent = name.childNodes[0].nodeValue;
    const score = XMLDoc.getElementsByTagName('m')[0];
    const scoreContent = score.childNodes[0].nodeValue;
    const li = XMLDoc.createElement("li");
    li.innerText = nameContent + " : " + scoreContent;
    ulElement.append(li);

}