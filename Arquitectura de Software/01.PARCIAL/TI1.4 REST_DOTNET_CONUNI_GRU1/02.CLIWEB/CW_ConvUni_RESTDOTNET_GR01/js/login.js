document.getElementById("loginForm").addEventListener("submit", function(e) {
    e.preventDefault();

    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();
    const errorMsg = document.getElementById("error");

    if(username === "MONSTER" && password === "MONSTER9") {
        // Login correcto
        window.location.href = "conversion.html";
    } else {
        errorMsg.textContent = "Usuario o contraseña incorrectos";
        document.getElementById("password").value = "";
    }
});
