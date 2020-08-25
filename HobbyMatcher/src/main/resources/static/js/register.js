document.getElementById("submit").disabled = true;
var isUsernameValidated = false;
var isPasswordValidated = false;

function isUnique()
{
    console.log("called");
    var username = document.getElementById("username").value;
    var url = "/api/isunique/" + username;

    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", url,  true);

    console.log(url);
    xhttp.onreadystatechange = function() {
        if (xhttp.readyState === 4) {
            console.log(xhttp.response);
            if(xhttp.response == "true")
            {
                console.log("green");
                document.getElementById("username-alert").style.display="none";
                isUsernameValidated = true;
            }
            else
            {
                document.getElementById("username-alert").style.display="block";
                console.log("red");
                isUsernameValidated = false;
            }

            // document.getElementById("username-alert").style.display="none";
            // isUsernameValidated = true;
            if(isUsernameValidated && isPasswordValidated)
            {
                document.getElementById("submit").disabled = false;
            }
            else
            {
                document.getElementById("submit").disabled = true;
            }

        }
    }

    xhttp.send();
}

function matchPassword()
{
    var password = document.getElementById("password").value;
    var confirmPassword = document.getElementById("confirm-password").value;

    if(confirmPassword!="")
    {
        if(password===confirmPassword)
        {
            document.getElementById("password-alert").style.display="none";
            isPasswordValidated = true;
        }
        else
        {
            document.getElementById("password-alert").style.display="block";
            isPasswordValidated = false;
        }
    }

        if(isUsernameValidated && isPasswordValidated)
        {
            document.getElementById("submit").disabled = false;
        }
        else
        {
            document.getElementById("submit").disabled = true;
        }

}

function matchConfirmPassword()
{
    var password = document.getElementById("password").value;
    var confirmPassword = document.getElementById("confirm-password").value;

        if(password===confirmPassword)
        {
            document.getElementById("password-alert").style.display="none";
            isPasswordValidated = true;
        }
        else
        {
            document.getElementById("password-alert").style.display="block";
            isPasswordValidated = false;
        }

        if(isUsernameValidated && isPasswordValidated)
        {
            document.getElementById("submit").disabled = false;
        }
        else
        {
            document.getElementById("submit").disabled = true;
        }

}