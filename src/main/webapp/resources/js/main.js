$(document).ready(function() {
	$('.toggle').click(function() {
		$('.menu').toggleClass('active');
	});
});

function checkReportParams() {
	var reportId = PF("reportValues").value;
	var empCode = document.getElementById("empCode").value;
	if (reportId == "2") {
		if (empCode == "") {
			alert("Please select the Employee Code");
			return false;
		}
	}
	return true;
}