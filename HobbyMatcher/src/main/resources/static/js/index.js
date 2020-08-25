function randomSelector()
{
    if(hobbies.length === 0)
    {
        document.getElementById("random-hobby").style.display="none";
        document.getElementById("no-random-hobby").style.display="block";
    }
    else
    {
        console.log("before");
        document.getElementById("no-random-hobby").style.display="none";
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


}
