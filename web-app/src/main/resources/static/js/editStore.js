let addRowDefault = function () {
        let listName = 'postCourses'; //list name in Catalog.class
        let fieldsNames = ['post', 'course','delete']; //field names from Movie.class
        let rowIndex = document.querySelectorAll('.item').length; //we can add mock class to each movie-row
        let row = document.createElement('div');
        row.classList.add('item', 'order_course','col-md-12' );

        fieldsNames.forEach((fieldName) => {
        	let rowPost = document.createElement('div');
        	if(fieldName === 'post'){
            	rowPost.classList.add('form-group', 'col-md-5','div-input-post');
            	let rowSpanPost = document.createElement('div');
            	rowSpanPost.classList.add('col-md-5','title-input-post');
            	let spanPost = document.createElement('span');
            	spanPost.textContent = "便/Post";
            	let rowInputPost = document.createElement('div');
            	rowInputPost.classList.add('col-md-7','edit-checkbox', 'title-input-post');
            	let inputPost = document.createElement('input');
            	inputPost.classList.add('form-control');
            	inputPost.type='text';
            	inputPost.id = fieldName + rowIndex;
            	inputPost.setAttribute('name',listName + '[' + rowIndex + '].' + fieldName);
            	rowSpanPost.appendChild(spanPost);
            	rowInputPost.appendChild(inputPost);
                rowPost.appendChild(rowSpanPost);
                rowPost.appendChild(rowInputPost);
        	}
        	let rowCourse = document.createElement('div');
        	if(fieldName === 'course'){
            	rowCourse.classList.add('form-group', 'col-md-6','div-input-post');
            	let rowSpanCourse = document.createElement('div');
            	rowSpanCourse.classList.add('col-md-6','title-input-post');
            	let spanCourse = document.createElement('span');
            	spanCourse.textContent = "コース/Course";
            	let rowInputCourse = document.createElement('div');
            	rowInputCourse.classList.add('col-md-6','edit-checkbox', 'title-input-post');
            	let inputCourse = document.createElement('input');
            	inputCourse.classList.add('form-control');
            	inputCourse.type='text';
            	/*inputCourse.id = listName + rowIndex + '.' + fieldName;*/
            	inputCourse.id = fieldName + rowIndex;
            	inputCourse.setAttribute('name',listName + '[' + rowIndex + '].' + fieldName);
            	rowSpanCourse.appendChild(spanCourse);
            	rowInputCourse.appendChild(inputCourse);
                rowCourse.appendChild(rowSpanCourse);
                rowCourse.appendChild(rowInputCourse);
        	}
        	let rowDelete = document.createElement('div');
        	if(fieldName === 'delete'){
            	rowDelete.classList.add('col-md-1');
            	let rowDeleteButton = document.createElement('button');
            	rowDeleteButton.type='submit';
            	rowDeleteButton.id='delete';
            	rowDeleteButton.textContent = "x";
            	rowDelete.appendChild(rowDeleteButton);
        	}

            row.appendChild(rowPost);
            row.appendChild(rowCourse);
            row.appendChild(rowDelete);

        });

        document.getElementById('postCourseList').appendChild(row);


};

let addRow = function () {
    let listName = 'postCourses'; //list name in Catalog.class
    let fieldsNames = ['post', 'course','delete']; //field names from Movie.class
    let rowIndex = document.querySelectorAll('.item').length; //we can add mock class to each movie-row
    let row = document.createElement('div');
    row.classList.add('item', 'order_course','col-md-12' );
    fieldsNames.forEach((fieldName) => {
    	let rowPost = document.createElement('div');
    	if(fieldName === 'post'){
        	rowPost.classList.add('form-group', 'col-md-5','div-input-post');
        	let rowSpanPost = document.createElement('div');
        	rowSpanPost.classList.add('col-md-5','title-input-post');
        	let spanPost = document.createElement('span');
        	spanPost.textContent = "便/Post";
        	let rowInputPost = document.createElement('div');
        	rowInputPost.classList.add('col-md-7','edit-checkbox', 'title-input-post');
        	let inputPost = document.createElement('input');
        	inputPost.classList.add('form-control','field-error');
        	inputPost.type='text';
        	inputPost.id = fieldName + rowIndex;
        	inputPost.setAttribute('name',listName + '[' + rowIndex + '].' + fieldName);

        	let emPost = document.createElement('em');
        	emPost.classList.add('error');
        	emPost.textContent ='post is required';

        	rowSpanPost.appendChild(spanPost);
        	rowInputPost.appendChild(inputPost);
        	rowInputPost.appendChild(emPost);

            rowPost.appendChild(rowSpanPost);
            rowPost.appendChild(rowInputPost);
    	}
    	let rowCourse = document.createElement('div');
    	if(fieldName === 'course'){
        	rowCourse.classList.add('form-group', 'col-md-6','div-input-post');
        	let rowSpanCourse = document.createElement('div');
        	rowSpanCourse.classList.add('col-md-6','title-input-post');
        	let spanCourse = document.createElement('span');
        	spanCourse.textContent = "コース/Course";
        	let rowInputCourse = document.createElement('div');
        	rowInputCourse.classList.add('col-md-6','edit-checkbox', 'title-input-post');
        	let inputCourse = document.createElement('input');
        	inputCourse.classList.add('form-control','field-error');
        	inputCourse.type='text';
        	inputCourse.id = fieldName + rowIndex;
        	inputCourse.setAttribute('name',listName + '[' + rowIndex + '].' + fieldName);
        	let emCourse = document.createElement('em');
        	emCourse.classList.add('error');
        	emCourse.textContent ='course is required';
        	rowSpanCourse.appendChild(spanCourse);
        	rowInputCourse.appendChild(inputCourse);
        	rowInputCourse.appendChild(emCourse);
            rowCourse.appendChild(rowSpanCourse);
            rowCourse.appendChild(rowInputCourse);
    	}
    	let rowDelete = document.createElement('div');
    	if(fieldName === 'delete'){
        	rowDelete.classList.add('col-md-1');
        	let rowDeleteButton = document.createElement('button');
        	rowDeleteButton.type='submit';
        	rowDeleteButton.id='delete';
        	rowDeleteButton.textContent = "x";
        	rowDelete.appendChild(rowDeleteButton);
    	}

        row.appendChild(rowPost);
        row.appendChild(rowCourse);
        row.appendChild(rowDelete);


    });

    document.getElementById('postCourseList').appendChild(row);

  /*  let postCourses = document.querySelectorAll('.item');
    Array.prototype.forEach.call(postCourses, function(elements, index) {
    	alert(elements);
    });*/

};

let checkFormValidate = function(){

}

$(document).ready(function(){
	$("body").on("click","#save",function(event){


        $("#postCourseList").closest("body").find(".item").each(function(index1){
            $(this).find("input[type=text]").each(function(index){
            	if(index === 0){
                    let post = "post" + index1;
                    $(this).attr("id", post);
				}
                if(index === 1){
                    let course = "course" + index1;
                    $(this).attr("id", course);
                }


            })
        })


		let checkForm = true;
		var code = $("#code").val();
		if(code === "" || code.trim() === ""){
			$(this).closest("body").find(".button-submit").find("button").removeAttr("data-toggle").removeAttr("data-target");
			$("#errorPost").removeClass("error").addClass("error_show");
			checkForm = false;
		}else{
			$("#errorPost").removeClass("error_show").addClass("error");
		}

		var course = $("#course").val();
		if(course === "" || course.trim() === ""){
			$(this).closest("body").find(".button-submit").find("button").removeAttr("data-toggle").removeAttr("data-target");
			$("#errorCourse").removeClass("error").addClass("error_show");
			checkForm = false;
		}else{
			$("#errorCourse").removeClass("error_show").addClass("error");
		}

		$("#postCourseList").closest("body").find(".item").each(function(index1){

			$(this).find("input[type=text]").each(function(index){
 				if(0 === index){
					let postIndex = '#post' + index1;
					let postvalue =$(this).closest("div").find(postIndex).val();
					if(postvalue === "" || postvalue.trim() === ""){
						$(this).closest("body").find(".button-submit").find("button").removeAttr("data-toggle").removeAttr("data-target");
						$(this).closest("div").find("em").removeClass("error").addClass("error_show");
						checkForm = false;
					}else{
						/*$(this).closest("body").find(".button-submit").find("button").attr("data-toggle","modal").attr("data-target","#exampleModal");*/
						$(this).closest("div").find("em").removeClass("error_show").addClass("error");
					}

				}

				if(1 === index){
					let courseIndex = '#course' + index1;
					let coursevalue =$(this).closest("div").find(courseIndex).val();
					console.log(coursevalue);
					if(coursevalue === "" || coursevalue.trim() === ""){
						$(this).closest("body").find(".button-submit").find("button").removeAttr("data-toggle").removeAttr("data-target");
						$(this).closest("div").find("em").removeClass("error").addClass("error_show");
						checkForm = false;
					}else{
						$(this).closest("div").find("em").removeClass("error_show").addClass("error");
					}

				}


			})

		})
		if(checkForm === false){
			event.preventDefault();
		}else{
			$(this).closest("body").find(".button-submit").find("button").attr("data-toggle","modal").attr("data-target","#exampleModal");
		}

	})

	$("body").on("click","#delete",function(event){
		event.preventDefault();
		$(this).closest(".item").remove();

		$("#postCourseList").find(".item").each(function(index1){
			$(this).find("input").each(function(index){

				if(0 === index){
					$(this).attr('name','postCourses[' + parseInt(index1) + '].post');
					$(this).attr('id','post' + parseInt(index1));
				}else if(1 === index){
					$(this).attr('name','postCourses[' + parseInt(index1) + '].course');
					$(this).attr('id','course' + parseInt(index1));
				}

			})

		})
	})


	$("body").on("click","#add",function(event){
		event.preventDefault();
		addRow();

		$(this).closest("body").find(".item").each(function(index1){
			$(this).find("input").each(function(index){

				if(0 === index){
					$(this).attr('name','postCourses[' + parseInt(index1) + '].post');
					$(this).attr('id','post' + parseInt(index1));
				}else if(1 === index){
					$(this).attr('name','postCourses[' + parseInt(index1) + '].course');
					$(this).attr('id','course' + parseInt(index1));
				}

			})

		})
	})
})