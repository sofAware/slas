const txtCsrf = document.querySelector('#csrf');
const txtTitle = document.querySelector('#inputTitle');
const header = txtCsrf.getAttribute('data-header-name');
const token = txtCsrf.getAttribute('value');

let rememberFileRemove = false;
const chkFileRemove = document.querySelector('input.form-check-input');
const syllabusId = document.querySelector('option[selected]').getAttribute('value');

const video = document.querySelector("#my-video");

document.querySelector("#file").onchange = (event) => {
    let file = event.target.files[0];
    let blobURL = URL.createObjectURL(file);
    video.src = blobURL;

    if (!txtTitle.value)
        txtTitle.value = file.name.substring(0, file.name.length - 4);
};

// 파일 수정 이벤트
if (document.querySelector('div.form-check')) {
    const lblFileRemove = document.querySelector('label.form-check-label');

    // 체크시 취소선 추가
    chkFileRemove.addEventListener('change', e => {
        if (rememberFileRemove = e.target.checked) {
            lblFileRemove.classList.add('text-decoration-line-through');
        } else {
            lblFileRemove.classList.remove('text-decoration-line-through');
        }
    })

    // 파일 선택시 자동 삭제 체크
    document.querySelector('input[type=file]').addEventListener("change", e => {
        if (e.target.value) {
            lblFileRemove.classList.add('text-decoration-line-through');
            chkFileRemove.checked = true;
            chkFileRemove.disabled = true;
        } else {
            lblFileRemove.classList.remove('text-decoration-line-through');
            chkFileRemove.checked = rememberFileRemove;
            chkFileRemove.disabled = false;
        }
    });
}

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
    // 파일 삭제 여부
    if (chkFileRemove && chkFileRemove.disabled)
        formData.append("deleteFile", "true");

    fetch('', {
        method: 'POST',
        body: formData
    }).then(
        res => location.href = res.url
    ).catch(
        error => console.error(error)
    );
});