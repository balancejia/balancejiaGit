$(function(){
	  //change the row style when the checkbox is checked in the row
	$('table tr td input[type=checkbox]').change(function() {
		var inRow = $(this).closest('tr');
		var  isChecked=this.checked;
		var isAll =$(this).closest('table').find('tr td input[type=checkbox]:checked').length==$(this).closest('table').find('tr td input[type=checkbox]').length;
		isAll = $(this).closest('table').find('tr td input[type=checkbox]:checked').length==0?true:isAll;
		if(isAll){
			 $(this).closest('table').find('tr th input[type=checkbox]').each(function(i,item){
				  this.checked=isChecked;
			     });
		}
		if (isChecked) {
			inRow.addClass('selected');
		}
		else {
			inRow.removeClass('selected');
		}
	});
	
	//select all rows 
	$('table tr th input[type=checkbox]').change(function() {
	var isChecked =this.checked;
		  $(this).closest('table').find('tr td input[type=checkbox]').each(function(i,item){
			  this.checked=isChecked;
		    	$(this).trigger('change');
		     });
	});
	//--Invert
/*	$('.Invert').click(function(){
		alert("aa");
		  $(this).closest('table').find('tr td input[type=checkbox]').each(function(i,item){
			  this.checked=!this.checked;
		    	$(this).trigger('change');
		     });
	});*/
});
