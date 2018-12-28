function isEmpty(input) {
    return input == null || input === "" || input.trim() === "";
}

function isNotEmpty(input) {
    return !isEmpty(input);
}

function isExistSpaceWhite(input){
    return input.indexOf(' ');
}

