const units = {
    mass: ["kg","g","lb","oz"],
    length: ["m","cm","km","ft"],
    temperature: ["c","f","k","r"]
};

const categorySelect = document.getElementById("category");
const fromSelect = document.getElementById("from");
const toSelect = document.getElementById("to");
const resultEl = document.getElementById("result");

// Actualiza los dropdowns de unidades según categoría
categorySelect.addEventListener("change", () => {
    const cat = categorySelect.value;

    // Limpia y agrega placeholder
    fromSelect.innerHTML = '<option value="">Seleccione unidad</option>';
    toSelect.innerHTML = '<option value="">Seleccione unidad</option>';

    if(units[cat]){
        units[cat].forEach(u => {
            const optionFrom = document.createElement("option");
            optionFrom.value = u;
            optionFrom.textContent = u.toUpperCase();
            fromSelect.appendChild(optionFrom);

            const optionTo = document.createElement("option");
            optionTo.value = u;
            optionTo.textContent = u.toUpperCase();
            toSelect.appendChild(optionTo);
        });
    }
});

// Manejo del form
document.getElementById("conversionForm").addEventListener("submit", async function(e){
    e.preventDefault();
    resultEl.textContent = "";
    resultEl.style.color = "green";

    const category = categorySelect.value;
    const from = fromSelect.value;
    const to = toSelect.value;
    const value = parseFloat(document.getElementById("value").value);

    if(!category || !from || !to){
        resultEl.textContent = "Por favor, seleccione todas las opciones.";
        resultEl.style.color = "red";
        return;
    }

    if(isNaN(value)){
        resultEl.textContent = "Ingrese un valor numérico válido.";
        resultEl.style.color = "red";
        return;
    }

    try{
        const response = await fetch("http://localhost:5069/api/Convert", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ category, from, to, value })
        });

        if(response.ok){
            const data = await response.json();
            resultEl.textContent = `${value} ${from.toUpperCase()} = ${data.result} ${to.toUpperCase()}`;
            resultEl.classList.add("flash");
            setTimeout(()=> resultEl.classList.remove("flash"), 800);
        } else {
            resultEl.textContent = "Error al conectarse con el servidor.";
            resultEl.style.color = "red";
        }
    } catch (err){
        resultEl.textContent = "Error: No se pudo conectar al servidor.";
        resultEl.style.color = "red";
    }
});
