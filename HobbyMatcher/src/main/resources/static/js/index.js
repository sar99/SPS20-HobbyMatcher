function sleep(milliseconds) {
  const date = Date.now();
  let currentDate = null;
  do {
    currentDate = Date.now();
  } while (currentDate - date < milliseconds);
}


function randomSelector()
{
    // console.log(hobbies);
    // var randomCard = document.getElementById("random-hobby");

    // randomCard.style.display = "block";

    // var i =0, k = 20;
    // while(k--)
    // {

    //     i = Math.floor((Math.random() * hobbies.length));
    //     console.log(i);
    //     // randomCard.childNodes[0] = 
    //     var url = "/hobbies/" + hobbies[i].id;
    //     document.getElementById("random-title").innerHTML = hobbies[i].name; 
    //     document.getElementById("random-url").setAttribute("href", url);
    //     sleep(750);

    // }

    console.log("before");
    var randomCard = document.getElementById("random-hobby");
    randomCard.style.display = "block";

    console.log("after");
    var k = 0, i;

    function counter() {
        k++;
        i = Math.floor((Math.random() * hobbies.length));
        console.log(i);
        // randomCard.childNodes[0] = 
        var url = "/hobbies/" + hobbies[i].id;
        document.getElementById("random-title").innerHTML = hobbies[i].name; 
        document.getElementById("random-url").setAttribute("href", url);
        if (k <= 20) {
            setTimeout(counter, 150);
        }
    }

    counter();


}
