function isEmpty(input) {
    return input == null || input === "" || input.trim() == "";
}

function isNotEmpty(input) {
    // return input != null && input != "" && input.trim() != "";
    return !isEmpty(input);
}