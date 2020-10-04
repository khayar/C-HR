$(document).ready(function() {
	$('.toggle').click(function() {
		$('.menu').toggleClass('active');
	});
});

function checkReportParams() {
	var reportId = PF('reportValues').value;
	var empCode = document.getElementById("empCode");
	if (reportId == "2") {
		if (empCode !== null && empCode.value !== null)
			alert("Please select the Employee Code");
		return false;
	}
	return true;
}