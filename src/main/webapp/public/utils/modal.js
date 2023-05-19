// Get the modal
var modal = document.getElementById("modalWindow");

// Get the <span> element that closes the modal
var closeBtn = document.getElementById("modal_close_btn");

// When the user clicks on <span> (x), close the modal
closeBtn.onclick = function() {
    modal.style.display = "none";
}

// When the user clicks anywhere outside the modal, close it
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

function openModal(cback){
    document.getElementById("modal_content_body").innerHTML = "";
    document.getElementById("modal_title").innerHTML = "";
    document.getElementById("modal_footer").innerHTML = "";

    cback();
    modal.style.display = "block";
}

function openConfirmPrompt(text, cback){
    openModal(function () {
        var modal = document.getElementById("modal_content_body");
        var modalTitle = document.getElementById("modal_title");

        modalTitle.innerHTML = "Confirm operation";

        modal.innerHTML = `
            <h6 class="text-start">${text}</h6>
        `;

        var confirm_btn = document.createElement("button");
        confirm_btn.textContent = "Confirm";
        confirm_btn.className = "btn btn-danger";

        var cancel_btn = document.createElement("button");
        cancel_btn.textContent = "Cancel";
        cancel_btn.className = "btn btn-secondary";
        cancel_btn.addEventListener('click', (e) => {
            document.getElementById("modalWindow").style.display = "none";
        });

        modal.nextElementSibling.appendChild(confirm_btn);
        modal.nextElementSibling.appendChild(cancel_btn);

        confirm_btn.addEventListener('click', (e) => {
            document.getElementById("modalWindow").style.display = "none";
            cback();
        });
    });
}
