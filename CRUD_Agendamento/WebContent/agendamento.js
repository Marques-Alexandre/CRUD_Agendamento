
var idatual = 0;
var modalCadastro;
var modalAlerta;
var modalExcluir;


window.onload = function(e){
    listar();
}

function listar() {
    // Limpar tabela
    var tab = document.getElementById("tabela");
    for (var i = tab.rows.length - 1; i>0; i --){
        tab.deleteRow(i);
    }
	
	var myHeaders = new Headers();
	myHeaders.append("AUTHORIZATION", "Bearer " + sessionStorage.getItem('meutoken'));
	
    fetch("http://localhost:8080/CRUD_Agendamento/consulta", {method: "GET", headers: myHeaders})
    .then(response => response.json())
    .then(data => {
        for(const item of data) {
            var tab = document.getElementById("tabela");
            var row = tab.insertRow(-1);
            row.insertCell(-1).innerHTML = item.idconsulta;
            row.insertCell(-1).innerHTML = formatarData(item.dataConsulta);
            row.insertCell(-1).innerHTML = item.horario;
            row.insertCell(-1).innerHTML = item.paciente;
            row.insertCell(-1).innerHTML = item.telefone;
            row.insertCell(-1).innerHTML = item.valor;
            row.insertCell(-1).innerHTML = "<button type='button' class='btn btn-primary' "
            + "onclick='alterar("+item.idconsulta+")'> "
            + "<i class='bi bi-pencil'></i> </button>"
            + "<button type='button' class='btn btn-danger' "
            + "onclick='excluir("+item.idconsulta+")'> "
            + "<i class='bi bi-trash'></i> </button>";
        }

    })
    .catch(error => console.log("Erro", error));
}

function novo() {
    idatual = 0;
    document.getElementById("txtData").value = "";
    document.getElementById("txtHorario").value = "";
    document.getElementById("txtNome").value = "";
    document.getElementById("txtTelefone").value = "";
    document.getElementById("numValor").value = "";

    modalCadastro = new bootstrap.Modal(document.getElementById("modalCadastro"));
    modalCadastro.show();
}

function alterar(id) {
    idatual = id;

	var myHeaders = new Headers();
	myHeaders.append("AUTHORIZATION", "Bearer " + sessionStorage.getItem('meutoken'));

    fetch("http://localhost:8080/CRUD_Agendamento/consulta/" + idatual, {method: "GET", headers: myHeaders})
    .then(response => response.json())
    .then(data => {

        document.getElementById("txtData").value = data.dataConsulta;
        document.getElementById("txtHorario").value = data.horario;
        document.getElementById("txtNome").value = data.paciente;
        document.getElementById("txtTelefone").value = data.telefone;
        document.getElementById("numValor").value = data.valor;

        modalCadastro = new bootstrap.Modal(document.getElementById("modalCadastro"));
        modalCadastro.show();
    })
    .catch(error => console.log("Erro", error));
    
}

function excluir(id) {
    idatual = id;
    document.getElementById("modalAlertaBody").style.backgroundColor = "#FFFFFF";
    document.getElementById("modalAlertaBody").innerHTML = "<h5>Confirma a exclusão do registro? </h5>"
    + '<button type="button" class="btn btn-primary" onclick="excluirSim()">Sim</button>'
    + '<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Não</button>';
    modalExcluir = new bootstrap.Modal(document.getElementById("modalAlerta"));
    modalExcluir.show();
}

function excluirSim() {
	
	var myHeaders = new Headers();
	myHeaders.append("AUTHORIZATION", "Bearer " + sessionStorage.getItem('meutoken'));
	
    fetch("http://localhost:8080/CRUD_Agendamento/consulta/" + idatual, {method: "DELETE", headers: myHeaders})
    .then(response => response.json())
    .then(result => {
        modalExcluir.hide();
        listar();
        if (result.success) {
            mostrarAlerta("Registro excluído com sucesso!", true);
        } else {
            mostrarAlerta("Falha ao excluir registro", false);
        }
    })
    .catch(error => console.log("Erro", error));
}

function salvar() {
    var l = {	
        idconsulta: idatual,
        dataConsulta: document.getElementById("txtData").value,
        horario: document.getElementById("txtHorario").value,
        paciente: document.getElementById("txtNome").value,
        telefone: document.getElementById("txtTelefone").value,
        valor: document.getElementById("numValor").value
    };

    var json = JSON.stringify(l);

    var url;
    var metodo;

    if (idatual == 0){
        url = "http://localhost:8080/CRUD_Agendamento/consulta";
        metodo = "POST";
    } else {
        url = "http://localhost:8080/CRUD_Agendamento/consulta/" + idatual;
        metodo = "PUT";
    }

	var myHeaders = new Headers();
	myHeaders.append("AUTHORIZATION", "Bearer " + sessionStorage.getItem('meutoken'));

    fetch(url, {method: metodo, body: json, headers: myHeaders})
    .then(response => response.json())
    .then(result => {
        mostrarAlerta(result.message, true);
        if(result.success) {
            modalCadastro.hide();
            listar();
        } else {
            mostrarAlerta(result.message, false);
        }
    })


}

function mostrarAlerta(msg, success) {
    if (success) {
        document.getElementById("modalAlertaBody").style.backgroundColor = "#E0F2F1";
    } else {
        document.getElementById("modalAlertaBody").style.backgroundColor = "#FFEBEE"
    }
    document.getElementById("modalAlertaBody").innerHTML = msg;
    modalAlerta = new bootstrap.Modal(document.getElementById("modalAlerta"));
    modalAlerta.show();
    window.setTimeout(function(){
        modalAlerta.hide();
    }, 3000);
}

function formatarData(databanco) {
	var vetor = databanco.split("-");
	return vetor[2] + '/' + vetor[1] + '/' + vetor[0]; 
}
