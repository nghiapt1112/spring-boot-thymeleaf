function search(page){
    var query = '/user/list?';
    var limitItems = document.getElementById('pageSizeSelect').value;
    if (!limitItems) {
        limitItems = 5;
    }
    query += '&limit=' + limitItems;

    var searchValue = document.getElementById('search-box').value ;
    if (searchValue) {
        query += '&search=' + searchValue;
    }
    if (page) {
        query += '&cp=' + page;
    } else {
        query += '&cp=' + 1;
    }
    window.location.replace(query);
}

function changePage(targetPage, currentPage, totalPage) {
    if (targetPage == 'pre') {
        currentPage -= 1;
    }
    if (currentPage == 0 || currentPage == totalPage) {
        console.log('Only onpage => do nothing')
        return;
    }
    if (targetPage == 'next') {
        currentPage += 1;
    }

    search(currentPage);
}