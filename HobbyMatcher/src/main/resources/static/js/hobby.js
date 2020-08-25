function getName(postId, userId)
{
    let id = "#name" + postId;
    let name = document.querySelector(id);

    let url = "/api/getname/" + userId;

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


function initLike(postId, hobbyId)
{
    let id = "#post" + postId;
    let button = document.querySelector(id);

    console.log("onload" + id);

    // button.classList.toggle("active");
    // button.classList.add("animated");
    // generateClones(button);

     let url = "/hobbies/" + hobbyId + "/post/api/isliked/" + postId;

    var xhttp = new XMLHttpRequest();
    var token = $("meta[name='_csrf']").attr("content"); 
    var header = $("meta[name='_csrf_header']").attr("content");
    xhttp.open("GET", url,  true);
    xhttp.setRequestHeader(header, token);

    xhttp.onreadystatechange = function() {
        if (xhttp.readyState === 4) {
            console.log(xhttp.response);
            if(xhttp.response == "true")
            {
                console.log("hi");
                button.classList.add("active");
            }

        }
    }

    xhttp.send();
}
