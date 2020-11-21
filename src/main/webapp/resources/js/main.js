$(document).ready(function() {
	$('.toggle').click(function() {
		$('.menu').toggleClass('active');
	});
});

function checkReportParams() {
	var reportId = PF("reportValues").value;
	var empCode = document.getElementById("empCode").value;
	var salaryMonth = PF("sm");
	
	alert("salary month" + salaryMonth);
	//Salary process slip
	if (reportId == "1" || reportId == "3") {
		if (salaryMonth == "") {
			alert("Please select the Salary Month");
			return false;
		}
	}
	//employee salary slip
	else if (reportId == "2") {
		if (empCode == "" && salaryMonth == "") {
			alert("Please select the Employee Code and Salary Month");
			return false;
		}
	}
	return true;
}