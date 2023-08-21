(function () {
    const modalDiv = document.createElement("div");
    modalDiv.classList.add("modal", "bg-secondary", "bg-opacity-50");
    modalDiv.setAttribute("id", "modalWindow");
    modalDiv.style.display = "none";

    const modalInnerDiv = document.createElement("div");
    modalInnerDiv.style.width="fit-content";
    modalInnerDiv.style.maxWidth="none";
    modalInnerDiv.style.minWidth="70%";
    modalInnerDiv.classList.add("modal-dialog", "modal-dialog-centered", "modal-dialog-scrollable");
    modalDiv.appendChild(modalInnerDiv);

    const modalContentDiv = document.createElement("div");
    modalContentDiv.classList.add("modal_content")
    modalContentDiv.classList.add("modal-content");
    modalInnerDiv.appendChild(modalContentDiv);

    const modalHeaderDiv = document.createElement("div");
    modalHeaderDiv.classList.add("modal-header");
    modalContentDiv.appendChild(modalHeaderDiv);

    const modalTitle = document.createElement("h1");
    modalTitle.setAttribute("id", "modal_title");
    modalTitle.classList.add("modal-title", "fs-5");
    modalHeaderDiv.appendChild(modalTitle);

    const modalCloseBtn = document.createElement("button");
    modalCloseBtn.setAttribute("type", "button");
    modalCloseBtn.setAttribute("data-bs-dismiss", "modal");
    modalCloseBtn.setAttribute("aria-label", "Close");
    modalCloseBtn.setAttribute("id", "modal_close_btn");
    modalCloseBtn.classList.add("btn-close");
    modalHeaderDiv.appendChild(modalCloseBtn);

    const modalBodyDiv = document.createElement("div");
    modalBodyDiv.setAttribute("id", "modal_content_body");
    modalBodyDiv.classList.add("modal-body");
    modalContentDiv.appendChild(modalBodyDiv);

    const modalFooterDiv = document.createElement("div");
    modalFooterDiv.setAttribute("id", "modal_footer");
    modalFooterDiv.classList.add("modal-footer");
    modalContentDiv.appendChild(modalFooterDiv);

    modalInnerDiv.appendChild(modalContentDiv);
    modalDiv.appendChild(modalInnerDiv);

    document.getElementById("container_div").appendChild(modalDiv);
})();



// Get the modal
const modal = document.getElementById("modalWindow");

// Get the <span> element that closes the modal
const closeBtn = document.getElementById("modal_close_btn");

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
        const modal = document.getElementById("modal_content_body");
        const modalTitle = document.getElementById("modal_title");

        modalTitle.innerHTML = "Confirm operation";

        modal.innerHTML = `
            <h6 class="text-start">${text}</h6>
        `;

        const confirm_btn = document.createElement("button");
        confirm_btn.textContent = "Confirm";
        confirm_btn.className = "btn btn-danger";

        const cancel_btn = document.createElement("button");
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

function openAlertPrompt(title, content){
    openModal(function () {
        const modal = document.getElementById("modal_content_body");
        const modalTitle = document.getElementById("modal_title");

        modalTitle.innerHTML = title;

        modal.innerHTML = `
            <h6 class="text-start">${content}</h6>
        `;

        const confirm_btn = document.createElement("button");
        confirm_btn.textContent = "Ok";
        confirm_btn.className = "btn btn-primary";

        modal.nextElementSibling.appendChild(confirm_btn);

        confirm_btn.addEventListener('click', (e) => {
            document.getElementById("modalWindow").style.display = "none";
        });
    });
}

function openSuccessPrompt(title, content){
    openModal(function () {
        const modal = document.getElementById("modal_content_body");
        const modalTitle = document.getElementById("modal_title");

        modalTitle.innerHTML = title;

        modal.innerHTML = `
            <h6 class="text-start">${content}</h6>
        `;

        const confirm_btn = document.createElement("button");
        confirm_btn.textContent = "Ok";
        confirm_btn.className = "btn btn-primary";

        modal.nextElementSibling.appendChild(confirm_btn);

        confirm_btn.addEventListener('click', (e) => {
            window.location.reload();
        });
    });
}
