function clickLike(postId, hobbyId)
{
    let id = "#post" + postId;
    let button = document.querySelector(id);

    console.log(id);

    button.classList.toggle("active");
    button.classList.add("animated");
    generateClones(button);

     let url = "/hobbies/" + hobbyId + "/post/like/" + postId;

    var xhttp = new XMLHttpRequest();
    var token = $("meta[name='_csrf']").attr("content"); 
    var header = $("meta[name='_csrf_header']").attr("content");
    xhttp.open("POST", url,  true);
    xhttp.setRequestHeader(header, token);
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

     let url = "/hobbies/" + hobbyId + "/post/isliked/" + postId;

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

function generateClones(button) {
  let clones = randomInt(2, 4);
  for (let it = 1; it <= clones; it++) {
    let clone = button.querySelector("svg").cloneNode(true),
      size = randomInt(5, 16);
    button.appendChild(clone);
    clone.setAttribute("width", size);
    clone.setAttribute("height", size);
    clone.style.position = "absolute";
    clone.style.transition =
      "transform 0.5s cubic-bezier(0.12, 0.74, 0.58, 0.99) 0.3s, opacity 1s ease-out .5s";
    let animTimeout = setTimeout(function() {
      clearTimeout(animTimeout);
      clone.style.transform =
        "translate3d(" +
        (plusOrMinus() * randomInt(10, 25)) +
        "px," +
        (plusOrMinus() * randomInt(10, 25)) +
        "px,0)";
      clone.style.opacity = 0;
    }, 1);
    let removeNodeTimeout = setTimeout(function() {
      clone.parentNode.removeChild(clone);
      clearTimeout(removeNodeTimeout);
    }, 900);
    let removeClassTimeout = setTimeout( function() {
      button.classList.remove("animated")
    }, 600);
  }
}


function plusOrMinus() {
  return Math.random() < 0.5 ? -1 : 1;
}

function randomInt(min, max) {
  return Math.floor(Math.random() * (max - min + 1) + min);
}
