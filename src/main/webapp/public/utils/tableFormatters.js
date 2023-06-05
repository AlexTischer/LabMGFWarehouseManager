function roleFormatter(value, row) {
    const roles = ['None', 'User', 'SuperUser', 'Admin'];

    const selectId = 'select_' + row.id;
    let selectHtml = '<select id="' + selectId + '" class="form-select">';

    roles.forEach(function(option) {
        const selected = (value === option) ? 'selected' : '';
        selectHtml += '<option value="' + roles.indexOf(option) + '" ' + selected + '>' + option + '</option>';
    });

    selectHtml += '</select>';

    return selectHtml;
}

function locationSelectFormatter() {
    const locations = ["Milano-Leonardo", "Pavia"];

    const locationSelect = document.createElement("select");
    locationSelect.setAttribute("class", "form-control");
    locationSelect.setAttribute("id", "addLocationSelect");
    locationSelect.setAttribute("name", "location");
    locationSelect.setAttribute("required", "required");

    for (let i = 0; i < locations.length; i++) {
        const option = document.createElement("option");
        option.setAttribute("value", i.toString());
        option.innerHTML = locations[i];
        locationSelect.appendChild(option);
    }

    return locationSelect;
}

function editActionFormatter(value, row) {
    return [
        '<a class="edit" href="javascript:void(0)" title="Edit">',
        'Edit',
        '</a>',
        '<a>    </a>',
        '<a class="remove" href="javascript:void(0)" title="Remove">',
        'Remove',
        '</a>'
    ].join('');
}