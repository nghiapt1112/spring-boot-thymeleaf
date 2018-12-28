function isEmpty(input) {
    return input === null || input === "";
}

function isNotEmpty(input) {
    return !isEmpty(input);
}

function isBlank(input) {
    return input.trim() === "";
}

function isNotBlank(input) {
    return !isBlank(input);
}

function isExistedSpaceWhite(input){
    return input.includes(" ");
}
function isNotExistedSpaceWhite(input){
    return !isExistedSpaceWhite(input);
}
function isExistedString(input1, input2){
    return input1.includes(input2);
}
function isNotExistedString(input1, input2){
    return !isExistedString(input1, input2);
}