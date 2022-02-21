const txtCsrf = document.querySelector('#csrf');
const header = txtCsrf.getAttribute('data-header-name');
const token = txtCsrf.getAttribute('value');

const syllabusId = document.querySelector('option[selected]').getAttribute('value');

const editor = new toastui.Editor({
    el: document.querySelector('#inputContent'),
    previewStyle: 'vertical',
    height: '560px',
});

// 기본 Base64 인코딩 이미지 후킹 제거 후 서버 업로드 방식 후킹 추가
editor.removeHook('addImageBlobHook');
editor.addHook('addImageBlobHook', (blob, callback) => {
    let formData = new FormData();
    formData.append("file", blob);

    fetch('/upload/syllabus/' + syllabusId, {
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

// 작성완료 버튼 이벤트
document.querySelector('#write').addEventListener('submit', e => {
    // 기본 이벤트 막고
    e.preventDefault();

    // 폼 데이터 가져오고 안채워진 내용 데이터 채우기
    const formData = new FormData(e.target);
    // 내용
    formData.append("content", editor.getMarkdown());
    // 학정번호
    formData.append(
        document.querySelector('select').getAttribute('name'),
        syllabusId
    );

    fetch('', {
        method: 'POST',
        body: formData
    }).then(
        res => location.href = res.url
    ).catch(
        error => console.error(error)
    );
});