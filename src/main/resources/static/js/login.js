// 회원 가입 창 시작
function signup() {
    Swal.fire({
        title: '사용자 등록',
        html: `
<div class="swal2-radio d-flex">
    <label>
        <input type="radio" id="professor" name="swal2-radio" value="ROLE_PROFESSOR">
        <span class="swal2-label">교수</span>
    </label>
    <label>
        <input type="radio" id="student" name="swal2-radio" value="ROLE_STUDENT" checked>
        <span class="swal2-label">학생</span>
    </label>
</div>
<input type="text" id="username" class="swal2-input" placeholder="학번">
<input type="password" id="password" class="swal2-input" placeholder="비밀번호">
<input type="text" id="name" class="swal2-input" placeholder="이름">
<input type="text" id="major" class="swal2-input" placeholder="전공">
<input type="number" id="semester" class="swal2-input" placeholder="학기">
`,
        confirmButtonText: '등록',
        showCloseButton: true,
        focusConfirm: false,
        preConfirm: () => {
            const radPro = Swal.getPopup().querySelector('#professor');
            const radStd = Swal.getPopup().querySelector('#student');
            const role = radPro.checked ? radPro.value : radStd.value;

            const id = Swal.getPopup().querySelector('#username').value;
            const password = Swal.getPopup().querySelector('#password').value;
            const name = Swal.getPopup().querySelector('#name').value;
            const major = Swal.getPopup().querySelector('#major').value;
            const semester = parseInt(Swal.getPopup().querySelector('#semester').value);

            if (!role || !id || !password || !name || !major || !semester) {
                Swal.showValidationMessage('정보를 모두 입력해주세요.');
            }
            return { role, id, password, name, major, semester };
        }
    }).then((result) => {
        if (!result.isConfirmed) return;

        // 회원 가입 전송
        fetch("/signup", {
            method: "POST",
            headers: {
                'Content-type': 'application/json; charset=utf-8'
            },
            body: JSON.stringify(result.value)
        }).then(res => {
            return res.json();
        }).then(json => {
            // 완료 메세지
            if (json.success) {
                Swal.fire(
                    `${result.value.name}님 환영합니다!`,
                    `학번: ${result.value.id}`,
                    'success'
                );
            } else {
                Swal.fire(
                    `사용자 등록에 실패하였습니다.`,
                    json.message,
                    'error'
                );
            }
        });
    });
}

// 회원 가입 버튼 이벤트 등록
const btnSignup = document.querySelector('#signup');
btnSignup.addEventListener('click', signup);

// 회원 가입 리디렉션 감지
if (new URL(window.location).hash === "#signup") {
    signup();
}
