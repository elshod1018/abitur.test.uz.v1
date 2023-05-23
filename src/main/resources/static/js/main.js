// var baseHost = 'http://localhost:8080';
var baseHost = 'https://abiturtestuz-v1.up.railway.app'
function update_s(subject_id) {
    fetch(baseHost + '/subjects/get/' + subject_id)
        .then(response => response.json())
        .then(subject => {
            document.getElementById('u_id').value = subject.id;
            document.getElementById('u_name').value = subject.name;
            document.getElementById('u_code').value = subject.code;
            document.getElementById('u_mandatory').checked = subject.is_mandatory;
            console.log(subject.is_mandatory);
        });
}

function delete_s(subject_id) {
    fetch(baseHost + '/subjects/get/' + subject_id)
        .then(response => response.json())
        .then(subject => {
            document.getElementById('d_id').value = subject.id;
            document.getElementById('d_name').innerText = subject.name;

        });
}

function update_question(question_id) {
    fetch(baseHost + '/questions/get/' + question_id)
        .then(response => response.json())
        .then(question => {
            document.getElementById('u_id').value = question.id;
            document.getElementById("u_questionSubject").selectedIndex = 1;
            document.getElementById('u_questionText').value = question.text;
            set_true_answer(question.answers);
            document.getElementById('u_firstAnswer').value = question.answers[0].text;
            document.getElementById('u_secondAnswer').value = question.answers[1].text;
            document.getElementById('u_thirdAnswer').value = question.answers[2].text;
            document.getElementById('u_fourthAnswer').value = question.answers[3].text;
        });
}

function set_true_answer(answers) {
    if (answers[0].is_true) {
        document.getElementById('u_firstAnswerRadio').checked = true;
    }
    if (answers[1].is_true) {
        document.getElementById('u_secondAnswerRadio').checked = true;
    }
    if (answers[2].is_true) {
        document.getElementById('u_thirdAnswerRadio').checked = true;
    }
    if (answers[3].is_true) {
        document.getElementById('u_fourthAnswerRadio').checked = true;
    }
}

function delete_question(question_id) {
    console.log(question_id);
    fetch(baseHost + '/questions/get/' + question_id)
        .then(response => response.json())
        .then(question => {
            document.getElementById('d_id').value = question.id;
            document.getElementById('d_text').innerText = question.text;
        });
}


function new_info(new_id) {
    fetch(baseHost + '/news/get/' + new_id)
        .then(response => response.json())
        .then(i_new => {
            document.getElementById('news-title').innerText = i_new.title;
            document.getElementById('news-body').innerHTML = "<p>" + i_new.body + "</p>";
            document.getElementById('news-time').innerHTML = "<p>Published date: " + i_new.createdAt + "</p>";
        });
}

function update_news(news_id) {
    fetch(baseHost + '/news/get/' + news_id)
        .then(response => response.json())
        .then(news => {
            document.getElementById('u_id').value = news.id;
            document.getElementById('u_title').value = news.title;
            document.getElementById('u_body').value = news.body;
        });
}

function delete_news(news_id) {
    fetch(baseHost + '/news/get/' + news_id)
        .then(response => response.json())
        .then(news => {
            console.log(news);
            document.getElementById('d_id').value = news.id;
            document.getElementById('d_title').innerText = news.title;
        });
}


function update_user(user_id) {
    fetch(baseHost + '/users/get/' + user_id)
        .then(response => response.json())
        .then(user => {
            document.getElementById('u_id').value = user.id;
            document.getElementById('u_name').innerText = user.firstName + "  " + user.lastName;
            document.getElementById("u_status").selectedIndex = 1;
        });
}

function show_second_subject() {
    var selectedSubjectId = document.getElementById("f_subjects_name").value;

    var htmlSelectElement = document.createElement("select");
    htmlSelectElement.setAttribute("id", "s_subjects_name");
    htmlSelectElement.setAttribute("name", "secondSubject");
    htmlSelectElement.setAttribute("style", "margin-left: 50px;width: 150px");
    const option = document.createElement("option");
    option.value = "-1";
    option.text = "None";
    htmlSelectElement.appendChild(option);

    if (selectedSubjectId !== "-1") {
        const selectOptions = document.getElementById("f_subjects_name");

        for (let i = 0; i < selectOptions.options.length; i++) {
            const option = selectOptions.options[i];
            if (option.value !== "-1" && option.value !== selectedSubjectId) {
                fetch(baseHost + '/subjects/get/' + option.value)
                    .then(response => response.json())
                    .then(subject => {
                        const option = document.createElement("option");
                        option.value = `${subject.id}`;
                        option.text = `${subject.name}`;
                        htmlSelectElement.appendChild(option);
                    });
            }
        }
    }
    document.getElementById("s_subjects_name").replaceWith(htmlSelectElement);
}

function start_test() {
    var f_s_i = document.getElementById("f_subjects_name").value;
    var s_s_i = document.getElementById("s_subjects_name").value;
    var mandatory = document.getElementById("flexSwitchCheckDefault").checked;
    var date = 0;
    if (f_s_i !== "-1") {
        date += 60;
    }
    if (s_s_i !== "-1") {
        date += 60;
    }
    if (mandatory === true) {
        date += 60;
    }
    document.getElementById("f_subject_id").value = f_s_i;
    document.getElementById("s_subject_id").value = s_s_i;
    document.getElementById("is_mandatory").value = mandatory;
    document.getElementById("test_time").innerText = date;
}

function get_session_subject_questions(testSession_id, subject_id) {
    fetch(baseHost + '/test/get/' + testSession_id + '/' + subject_id)
        .then(response => response.json())
        .then(solveQuestions => {
            document.getElementById("one_question").innerHTML = "";
            const countRadioButtons = solveQuestions.length;
            const myDiv = document.createElement("div");
            myDiv.className = "btn-group";
            myDiv.id = "questions_group";
            myDiv.role = "group";
            myDiv.setAttribute("aria-label", "Basic radio toggle button group");
            for (let i = 1; i <= countRadioButtons; i++) {
                const solveQuestion = solveQuestions[i - 1];
                const radio = document.createElement("input");
                radio.type = "radio";
                radio.className = "btn-check";
                radio.name = "question";
                radio.id = "question" + i;
                radio.autocomplete = "off";
                radio.onchange = function () {
                    get_subject_question(testSession_id, subject_id, solveQuestion.questionId);
                };
                const label = document.createElement("label");
                label.className = "btn btn-outline-primary";
                label.htmlFor = "question" + i;
                label.innerHTML = i;
                myDiv.appendChild(radio);
                myDiv.appendChild(label);
            }
            document.getElementById("questions_group").replaceWith(myDiv);
        });
}

function get_subject_question(testSession_id, subject_id, question_id) {
    fetch(baseHost + '/test/get/' + testSession_id + '/' + subject_id + '/' + question_id)
        .then(response => response.json())
        .then(question => {
            var one_question_div = document.createElement("div");
            one_question_div.className = "mb-3";
            one_question_div.id = "one_question";

            var question_div = document.createElement("div");
            question_div.className = "mb-3";

            var question_text_div = document.createElement("p");
            question_text_div.style = "font-size: 25px;color: rgb(29, 45, 91)";
            question_text_div.id = "questionText";
            question_text_div.innerText = question.text;
            question_div.appendChild(question_text_div);

            var counter = 1;
            question.answers.forEach(answer => {
                var answer_div = document.createElement("div");
                answer_div.className = "form-check";

                var answer_radio = document.createElement("input");
                answer_radio.className = "form-check-input";
                answer_radio.style = "font-size: 20px;color: #0a53be";
                answer_radio.type = "radio";
                answer_radio.name = "userAnswer";
                answer_radio.id = "answer" + counter;
                answer_radio.value = '-1';
                if (answer.id === question.userAnswerId) {
                    answer_radio.checked = true;
                }
                answer_radio.onchange = function () {
                    update_user_answer(question.testSessionId, question.subjectId, question.id, answer.id);
                };

                var answer_text = document.createElement("p");
                answer_text.style = "font-size: 20px;color: #0a53be";
                answer_text.id = "answer" + counter;
                answer_text.innerText = answer.text;
                answer_div.appendChild(answer_radio);
                answer_div.appendChild(answer_text);
                question_div.appendChild(answer_div);
                counter++;
            });
            one_question_div.appendChild(question_div);
            document.getElementById("one_question").replaceWith(one_question_div);
        });
}


function update_user_answer(test_session_id, subject_id, question_id, answer_id) {
    fetch(baseHost + '/test/update/' + test_session_id + '/' + subject_id + '/' + question_id + '/' + answer_id)
        .then(response => response.text())
        .then(response => {
            console.log(response);
        });
}

function draw_password_area() {
    document.getElementById("change_phone").innerHTML = "";

    var reset_password = document.getElementById("reset_password");
    reset_password.innerHTML = `
    
    <form method="post" action="/users/update/password" style="width: 100%;margin: 30px auto 0" accept-charset="UTF-8">
        <div class="mb-3">
            <label for="old_password">Old password:</label>
            <input class="form-control"
                   placeholder="Enter old password"
                   name="oldPassword"
                   type="password"
                   value=""
                   id="old_password">
        </div>
        <div class="mb-3">
            <label for="password">New password:</label>
            <input class="form-control" placeholder="Enter a new password" autocomplete="off" name="newPassword"
                   type="password" value="" id="password">
        </div>
        <div class="mb-3">
            <label for="password_confirmation">Confirm new password</label>
            <input class="form-control" placeholder="Enter a new password" name="newConfirmPassword"
                   type="password"
                   value="" id="password_confirmation">
        </div>
        <div class="mb-3" style="width: 90%;margin: auto 87%">
            <button type="submit" class="btn custom_btn">Submit</button>
        </div>
    </form>
`;
}

function draw_phone_area() {
    document.getElementById("reset_password").innerHTML = "";

    var change_phone = document.getElementById("change_phone");
    change_phone.innerHTML = `
    <form method="post" action="profile/update/phone" style="width: 100%;margin: 30px auto 0" accept-charset="UTF-8">
        <div class="mb-3">
            <label for="phone_number">Phone Number: </label>
            <input class="form-control"
                   placeholder="Enter New Phone Number"
                   name="phoneNumber"
                   type="text"
                   value=""
                   id="phone_number">
        </div>
        <div class="mb-3" style="width: 90%;margin: auto 87%">
            <button type="submit" class="btn custom_btn">Submit</button>
        </div>
    </form>
    `;
}





