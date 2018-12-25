let addRow = function () {
    let listName = 'postCourses';
    let fieldsNames = ['tenantId','postCourseId', 'storeId', 'createDate', 'createUser', 'post', 'course', 'delete'];
    let rowIndex = document.querySelectorAll('.item').length;
    let row = document.createElement('div');
    row.classList.add('item', 'order_course', 'col-md-12');
    fieldsNames.forEach((fieldName) => {
        let rowTenantId = document.createElement('input');
    if (fieldName === 'tenantId') {
        rowTenantId.type = 'hidden';
        rowTenantId.setAttribute('name', listName + '[' + rowIndex + '].' + fieldName);
    }
    let rowPostCourseId = document.createElement('input');
    if (fieldName === 'postCourseId') {
        rowPostCourseId.type = 'hidden';
        rowPostCourseId.setAttribute('name', listName + '[' + rowIndex + '].' + fieldName);
    }
    let rowStoreId = document.createElement('input');
    if (fieldName === 'storeId') {
        rowStoreId.type = 'hidden';
        rowStoreId.setAttribute('name', listName + '[' + rowIndex + '].' + fieldName);
    }
    let rowCreateDate = document.createElement('input');
    if (fieldName === 'createDate') {
        rowCreateDate.type = 'hidden';
        rowCreateDate.setAttribute('name', listName + '[' + rowIndex + '].' + fieldName);
    }
    let rowCreateUser = document.createElement('input');
    if (fieldName === 'createUser') {
        rowCreateUser.type = 'hidden';
        rowCreateUser.setAttribute('name', listName + '[' + rowIndex + '].' + fieldName);
    }
    let rowPost = document.createElement('div');
    if (fieldName === 'post') {
        rowPost.classList.add('form-group', 'col-md-5', 'div-input-post');
        let rowSpanPost = document.createElement('div');
        rowSpanPost.classList.add('col-md-5', 'title-input-post');
        let spanPost = document.createElement('span');
        spanPost.textContent = "便";
        let rowInputPost = document.createElement('div');
        rowInputPost.classList.add('col-md-7', 'edit-checkbox', 'title-input-post');
        let inputPost = document.createElement('input');
        inputPost.classList.add('form-control', 'field-error');
        inputPost.type = 'text';
        inputPost.id = fieldName + rowIndex;
        inputPost.setAttribute('name', listName + '[' + rowIndex + '].' + fieldName);

        let emPost = document.createElement('em');
        emPost.classList.add('error');
        emPost.textContent = '“便”は必須です。';

        rowSpanPost.appendChild(spanPost);
        rowInputPost.appendChild(inputPost);
        rowInputPost.appendChild(emPost);

        rowPost.appendChild(rowSpanPost);
        rowPost.appendChild(rowInputPost);
    }
    let rowCourse = document.createElement('div');
    if (fieldName === 'course') {
        rowCourse.classList.add('form-group', 'col-md-6', 'div-input-post');
        let rowSpanCourse = document.createElement('div');
        rowSpanCourse.classList.add('col-md-6', 'title-input-post');
        let spanCourse = document.createElement('span');
        spanCourse.textContent = "コース";
        let rowInputCourse = document.createElement('div');
        rowInputCourse.classList.add('col-md-6', 'edit-checkbox', 'title-input-post');
        let inputCourse = document.createElement('input');
        inputCourse.classList.add('form-control', 'field-error');
        inputCourse.type = 'text';
        inputCourse.setAttribute('name', listName + '[' + rowIndex + '].' + fieldName);
        rowSpanCourse.appendChild(spanCourse);
        rowInputCourse.appendChild(inputCourse);
        rowCourse.appendChild(rowSpanCourse);
        rowCourse.appendChild(rowInputCourse);
    }
    let rowDelete = document.createElement('div');
    if (fieldName === 'delete') {
        rowDelete.classList.add('col-md-1');
        let rowDeleteButton = document.createElement('button');
        rowDeleteButton.type = 'submit';
        rowDeleteButton.id = 'delete';
        rowDeleteButton.textContent = "x";
        rowDelete.appendChild(rowDeleteButton);
    }

    row.appendChild(rowPost);
    row.appendChild(rowCourse);
    row.appendChild(rowDelete);


})

    document.getElementById('postCourseList').appendChild(row);
};

$(document).ready(function () {
    $("body").on("click", "#save", function (event) {

        $("#postCourseList").closest("body").find(".item").each(function (index1) {
            $(this).find("input[type=text]").each(function (index) {
                if (index === 0) {
                    let post = "post" + index1;
                    $(this).attr("id", post);
                }
            })
        })

        let checkForm = true;
        var code = $("#code").val();
        if (isEmpty(code)) {
            $("#error_exitsted").removeClass("error_show").addClass("error");
            $("#errorCode").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#error_exitsted").removeClass("error_show").addClass("error");
            $("#errorCode").removeClass("error_show").addClass("error");
        }

        var name = $("#name").val();
        if (isEmpty(name)) {
            $("#errorName").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorName").removeClass("error_show").addClass("error");
        }

        $("#postCourseList").closest("body").find(".item").each(function (index1) {

            $(this).find("input[type=text]").each(function (index) {
                if (0 === index) {
                    let postIndex = '#post' + index1;
                    let postvalue = $(this).closest("div").find(postIndex).val();
                    if (isEmpty(postvalue)) {
                        $(this).closest("div").find("em").removeClass("error").addClass("error_show");
                        checkForm = false;
                    } else {
                        $(this).closest("div").find("em").removeClass("error_show").addClass("error");
                    }

                }

            })

        })
        if (checkForm === false) {
            event.preventDefault();
        }

    })

    $("body").on("click", "#delete", function (event) {
        event.preventDefault();
        $(this).closest(".item").remove();
        $("#postCourseList").find(".item").each(function (index1) {
            let lengthInputPostCourse = $(this).find('input').length;
            if (lengthInputPostCourse === 7) {
                $(this).find("input").each(function (index) {
                    if (0 === index) {
                        $(this).attr('name', 'postCourses[' + index1 + '].tenantId');
                    } else if (1 === index) {
                        $(this).attr('name', 'postCourses[' + index1 + '].postCourseId');
                    } else if (2 === index) {
                        $(this).attr('name', 'postCourses[' + index1 + '].storeId');
                    } else if (3 === index) {
                        $(this).attr('name', 'postCourses[' + index1 + '].createDate');
                    } else if (4 === index) {
                        $(this).attr('name', 'postCourses[' + index1 + '].createUser');
                    } else if (5 === index) {
                        $(this).attr('name', 'postCourses[' + index1 + '].post');
                        $(this).attr('id', 'post' + index1);
                    } else if (6 === index) {
                        $(this).attr('name', 'postCourses[' + index1 + '].course');
                    }

                })
            } else {
                $(this).find("input").each(function (index) {
                    if (0 === index) {
                        $(this).attr('name', 'postCourses[' + index1 + '].post');
                        $(this).attr('id', 'post' + index1);
                    } else if (1 === index) {
                        $(this).attr('name', 'postCourses[' + index1 + '].course');
                    }

                })
            }


        })
    })


    $("body").on("click", "#add", function (event) {
        event.preventDefault();
        addRow();

        $(this).closest("body").find(".item").each(function (index1) {
            let lengthInputPostCourse = $(this).find('input').length;
            if (lengthInputPostCourse === 7) {
                $(this).find("input").each(function (index) {
                    if (0 === index) {
                        $(this).attr('name', 'postCourses[' + index1 + '].tenantId');
                    } else if (1 === index) {
                        $(this).attr('name', 'postCourses[' + index1 + '].postCourseId');
                    } else if (2 === index) {
                        $(this).attr('name', 'postCourses[' + index1 + '].storeId');
                    } else if (3 === index) {
                        $(this).attr('name', 'postCourses[' + index1 + '].createDate');
                    } else if (4 === index) {
                        $(this).attr('name', 'postCourses[' + index1 + '].createUser');
                    } else if (5 === index) {
                        $(this).attr('name', 'postCourses[' + index1 + '].post');
                        $(this).attr('id', 'post' + index1);
                    } else if (6 === index) {
                        $(this).attr('name', 'postCourses[' + index1 + '].course');
                    }

                })
            }  else {
                $(this).find("input").each(function (index) {
                    if (0 === index) {
                        $(this).attr('name', 'postCourses[' + index1 + '].post');
                        $(this).attr('id', 'post' + index1);
                    } else if (1 === index) {
                        $(this).attr('name', 'postCourses[' + index1 + '].course');
                    }

                })
            }


        })
    })
})