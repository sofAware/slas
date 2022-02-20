const txtCsrf = document.querySelector('#csrf');
const header = txtCsrf.getAttribute('name');
const token = txtCsrf.getAttribute('value');

const editor = new toastui.Editor({
    el: document.querySelector('#inputContent'),
    previewStyle: 'vertical',
    height: '600px',
});

// 기본 Base64 인코딩 이미지 후킹 제거 후 서버 업로드 방식 후킹 추가
editor.removeHook('addImageBlobHook');
editor.addHook('addImageBlobHook', (blob, callback) => {
    let formData = new FormData();
    formData.append("file", blob);

    fetch('/upload', {
        method: 'POST',
        headers: {
            [header]: token
        },
        body: formData
    }).then(
        res => res.text()
    ).then(
        body => {
            if (body) callback(body, "image");
        }
    ).catch(
        error => console.error(error)
    );

    return false;
});