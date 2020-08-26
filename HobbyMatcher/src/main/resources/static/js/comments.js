function getName(postId, userId)
{
    let id = "#name" + postId;
    let name = document.querySelector(id);

    let url = "/api/getname/" + userId;
    console.log(url);


    var xhttp = new XMLHttpRequest();
    var token = $("meta[name='_csrf']").attr("content"); 
    var header = $("meta[name='_csrf_header']").attr("content");
    xhttp.open("GET", url,  true);
    xhttp.setRequestHeader(header, token);

    xhttp.onreadystatechange = function() {
        if (xhttp.readyState === 4) {
            console.log(xhttp.response);
            name.innerHTML = xhttp.response;
        }
    }

    xhttp.send();
}
