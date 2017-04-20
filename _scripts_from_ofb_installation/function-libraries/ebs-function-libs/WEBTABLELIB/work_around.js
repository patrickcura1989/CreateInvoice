

function checkCompExistance(firstCell, tableIndex, rowIndex, columnIndex, userTag) {

	var cell = getTableCell(firstCell, tableIndex, rowIndex, columnIndex);
	var elementExists = -1;
	
	if(cell) {
	
		userTag = userTag.toLowerCase();
		tag = userTag;
		
		if (userTag == "checkbox" || userTag == "radio"){
			tag="input";
		}
		
		var elements = cell.getElementsByTagName(tag);
	
		for(var elementIndex = 0; elementIndex < elements.length; elementIndex++) 
		{
			var element = elements[elementIndex ];
				
			if( tag == "img") {
				
				if(element.getAttribute('alt') != ''){
					return elementIndex;
				}else if(element.src != ''){
					return elementIndex;
				}
				
			}else if(tag == "input") {
				if(userTag == "checkbox")
				{
					if(element.type == "checkbox") 
					{
						return elementIndex;
					}
				}else if(userTag == "radio") 
				{
					if(element.type == "radio") 
					{
						return elementIndex;
					}
				} else {
					if(element.type != "hidden") 
					{
						return elementIndex;
					}
					
				}
							
			}else if(tag == "select") {
							
				return elementIndex;
				
			} else if(tag == "span") {
				
				if(element.text != undefined && element.text != ''){
					return elementIndex;
				}else if(element.innerText != undefined  && element.text != ''){
					return elementIndex;
				}else if(element.textContent != undefined  && element.text != ''){
					return elementIndex;
				}
							
			}else if(tag == "div") {
				
				if(element.text != undefined && element.text != ''){
					return elementIndex;
				}else if(element.innerText != undefined  && element.text != ''){
					return elementIndex;
				}else if(element.textContent != undefined  && element.text != ''){
					return elementIndex;
				}
							
			}else if(tag == "a") {
				return elementIndex;
			}
		}
	}
	
	return elementExists;
}




//Match the table by firstCell content.
//rowIndex are zero-based value.
//columnIndex are zero-based value.
//index are zero-based value.
//tag are the tagname of target element
function getValueByType(firstCell, tableIndex, rowIndex, columnIndex, userTag, index) {
	var cell = getTableCell(firstCell, tableIndex, rowIndex, columnIndex);
	if(cell) {
		userTag = userTag.toLowerCase();
		tag = userTag;
		
		if (userTag == "checkbox" || userTag == "radio"){
			tag="input";
		}
		
		if(tag == "td"){
			return cell.innerText;
		}

		var elements = cell.getElementsByTagName(tag);
		
		
		if(elements && index < elements.length) {
			var element = elements[index];
			
			
			if( tag == "img") {
				
				if(element.getAttribute('alt') != ''){
					return element.getAttribute('alt');
				}else if(element.src != ''){
					return element.src;
				}
				
			}else if(tag == "input") {
				if(userTag == "checkbox")
				{
					if(element.type == "checkbox") 
					{
						return element.checked;
					}
				}else if(userTag == "radio") 
				{
					if(element.type == "radio") 
					{
						return element.checked;
					}
				} else {
					if(element.type != "hidden") 
					{
						return element.value;
					}
					
				}
							
			}else if(tag == "select") {
							
				return element.options[element.selectedIndex].text;
				
			} else if(tag == "span") {
				
				if(element.text != undefined && element.text != ''){
					return element.text;
				}else if(element.innerText != undefined  && element.text != ''){
					return element.innerText;
				}else if(element.textContent != undefined  && element.text != ''){
					return element.textContent;
				}
							
			}else if(tag == "div") {
				
				if(element.text != undefined && element.text != ''){
					return element.text;
				}else if(element.innerText != undefined  && element.text != ''){
					return element.innerText;
				}else if(element.textContent != undefined  && element.text != ''){
					return element.textContent;
				}
							
			}else if(tag == "a") {
				return element.innerText;
				
			}
			
		}
	}
}


//Match the table by firstCell content.
//rowIndex are zero-based value.
//columnIndex are zero-based value.
//compIndex are zero-based value.
//tag are the tagname of target element
function clickElement(firstCell, tableIndex, rowIndex, columnIndex, compType, compValue, compIndex) {
	
	var cell = getTableCell(firstCell, tableIndex, rowIndex, columnIndex);
	
	if(cell) {
		
		var tag = compType.toLowerCase();
		var elements = cell.getElementsByTagName(tag);
		
		if(elements) {
			if(compValue != 'null'){
				
				for(var elementIndex = 0; elementIndex < elements.length; elementIndex++) {
					
					var element = elements[elementIndex];
					var elementExists = false;
										
					if( tag == "img") {
						var actCompValue = element.getAttribute('alt');
						
						if(actCompValue == compValue){
							elementExists=true;
						}
					} else if(tag == "a"){
						var actCompValue = element.innerText;
						//alert('actCompValue :'+actCompValue);
						//alert('compValue :'+compValue);
						if(actCompValue == compValue){
							elementExists=true;
						}
					}else if(tag == "button"){
						var actCompValue = element.getAttribute('title');;
						//alert('actCompValue :'+actCompValue);
						//alert('compValue :'+compValue);
						if(actCompValue == compValue){
							elementExists=true;
						}
					}
					
					// If element exists then click the element
					if(elementExists){
						element.click();
						break;
					}
				}		
			} else {
				var element = elements[compIndex];
				element.click();
			}
		}
	}
}

//Match the table by firstCell content.
//rowIndex are zero-based value.
//columnIndex are zero-based value.
//compIndex are zero-based value.
//tag are the tagname of target element
function setElement(firstCell, tableIndex, rowIndex, columnIndex, compType, valueToEnter, compIndex) {
	
	var elementExists = false;
	var expComponents = new Array();
	var cell = getTableCell(firstCell, tableIndex, rowIndex, columnIndex);
	
	if(cell) {
		
		userTag = compType.toLowerCase();
		tag = userTag;
		
		if (userTag == "checkbox" || userTag == "radio"){
			tag="input";
		}
		
		var elements = cell.getElementsByTagName(tag);

		//alert('element length :'+elements.length);
		
		/* Get all the componenttype specific components*/
		for(var elementIndex = 0; elementIndex < elements.length; elementIndex++) {
			
			var element = elements[elementIndex];
						
			if(userTag == "checkbox"){
				if (element.type == "checkbox"){
					expComponents.push(element);
				}
			}else if(userTag == "radio"){
				if (element.type == "radio"){
					expComponents.push(element);
				}
			}else if(userTag == "input"){
				
				if (element.type != "radio" && element.type != "checkbox" && element.type != "hidden" ){
					expComponents.push(element);
				}
			}else{
				expComponents.push(element);
			}
		}
		
		

		/* Setting all the required compenent type components like all checkboxes or all radio buttion or all inputs or all selectboxes with in a cell */
		elements = expComponents;
		
		
		if(elements) {
			//alert("input elements which are not hidden :"+elements.length)

			if(compIndex < elements.length){
			
				var element = elements[compIndex];
			
				//alert("input element count :"+elements.length)
			
				if(tag == "input") {
					if(userTag == "checkbox")
					{
						if(element.type == "checkbox") 
						{
							if(valueToEnter=='true'){
								element.checked = true;
							}else{
								element.checked = false;
							}
							
							element.fireEvent('onchange');
							element.fireEvent('onblur');
							element.fireEvent('onclick');
							elementExists = true;
						}
					}else if(userTag == "radio") 
					{				
						if(element.type == "radio") 
						{
							element.checked = true;
							element.fireEvent('onchange');
							element.fireEvent('onblur');
							element.fireEvent('onclick');
							elementExists = true;
						}
					} else if(userTag == "input") 
					{
						element.fireEvent('onclick');
						element.value = valueToEnter;
						element.fireEvent('onchange');
						element.fireEvent('onblur');
						//element.fireEvent('blur');
						//element.fireEvent('focusout');	
						elementExists = true;
					}
							
				}else if(tag == "select") {
					//alert("valueToEnter :"+valueToEnter);
					//alert("Select Lenth :"+element.length);
					var items = element.options;
					
					for(var selectIndex = 0; selectIndex < element.length; selectIndex++) 
					{
						if(items[selectIndex].text == valueToEnter)
						{
							//alert("Select Value :"+items[selectIndex].text);
							element.fireEvent('onclick');
							element.value = items[selectIndex].value;
							element.fireEvent('onchange');
							element.fireEvent('onblur');
							elementExists = true;
							break;
						}
					}
				}else if(tag == "textarea") {
					element.fireEvent('onclick');
					element.value = valueToEnter;
					element.fireEvent('onchange');
					element.fireEvent('onblur');
					elementExists = true;
				}
			} 
			
		}
	}
	
	return elementExists;
}


//Match the table by firstCell content.
//rowIndex are zero-based value.
//columnIndex are zero-based value.
function getTableCell(firstCell, tableIndex, rowIndex, columnIndex)
{
	/* Get Table */
	var table = getTable(firstCell, tableIndex);
	
	/* Get Table Rows */
	var rows = table.rows;
	
	//alert('rows.length ' + rows.length)
	if(rowIndex < rows.length) 
	{
		//alert('RowIndex :'+rowIndex);
		var rowStartIndex = findRowIndex(rows, firstCell);

		//alert('rowStartIndex  :'+rowStartIndex );
		var actualRowIndex = parseInt(rowStartIndex)+parseInt(rowIndex)-parseInt(1);

		//alert('actualRowIndex   :'+actualRowIndex);

		/* Get Specified Row based on rowIndex*/
		var row = rows[actualRowIndex];

		//alert ('row.cells.length '+row.cells.length);
		
		if(columnIndex < row.cells.length) 
		{
			/* Get Required Cell based on ColumnIndex in the specified row */
			var cell = row.cells[columnIndex];
			return cell;
		}

	}
}

function getTable(firstCell, tableIndex)
{
	var expTables = new Array();
	
	/* Get Tables in the Web Page*/
	var tables = document.getElementsByTagName("table");
	
	//alert('tables.length :'+tables.length)
	/* Navigate through Tables and get the required Table based on firstCell */
	for(var i = 0; i < tables.length; i++)
	{
		/* Get First Table */
		var table = tables[i];
		
		/* Get All Rows in the specifed Table*/
	    var rows = table.rows;
		
		if(rows.length > 0) {
			
			/* Get Column Row Number */
			var columnRow = findColumnsROW(rows);
			//alert('columnRow: '+columnRow )

			if(columnRow >= 0){
				
				var row = rows[columnRow];
				//var row = rows[0];

				var colLength = row.cells.length;
					
				/* 1. Compare the First Column with Expected Value
				   2. If Matched, Get the Required Tables */
			
				for(var colIndex = 0; colIndex < colLength; colIndex++)
				{
					var column = row.cells[colIndex];
					var columnValue = column.innerText;
					//alert('Table '+i+" Matched Column:"+column.innerText);
					if(column.innerText == firstCell) {
						//alert('Table '+i+" Matched Column:"+column.innerText);
						expTables.push(table);
						break;
					}	
				}	
			}	
		
	    }
	}
	
	/* Get the expTable based on index
		index=0 , means first Table
		index=1 , means second Table
	*/
	//alert(expTables.length)
	//if(expTables.length > tableIndex)
	//{
	//	var expTable = expTables[tableIndex];
	//}
	
	/**
		This is to navigate through the tables for identifying the column with 'td' tag.
	*/
	if(expTables.length == 0){
		
		for(var i = 0; i < tables.length; i++)
		{
			/* Get First Table */
			var table = tables[i];
		
			/* Get All Rows in the specifed Table*/
	    		var rows = table.rows;

			//alert('tables index :'+i)
		
			if(rows.length > 0) {
			
				/* Get Column Row Number */
				var columnRow = findColumnsRowsWithoutTH(rows, firstCell);
				
				if(columnRow >= 0){
				
					expTables.push(table);	
				}	
		
			}
		}
	}
	
	
	var expTable = expTables[tableIndex];
	
	return expTable;
}



function findRowIndex(rows, columnname)
{
	var rowStartIndex = -1;
	var rowLength = 0;
	var colIndex = -1;
	var columnHeaderExists = false;
	var noOfColumnHeders = 0;
	
	if(rows.length > 4){
		rowLength = 4;
	}else{
		rowLength = rows.length;
	}

	//alert("row Length :"+rowLength);
	// This method works only for the tables having "th"
	for(var rowIndex = 0; rowIndex < rowLength; rowIndex++){
	
		var row = rows[rowIndex];

		var colLength = row.cells.length;
		
		if(colLength > 1){
			colIndex = 1;
		}else{
			colIndex = 0;
		}
		
		//alert("row.cells[colIndex].tagName.toLowerCase() :"+row.cells[colIndex].tagName.toLowerCase());

		if (row.cells[colIndex].tagName.toLowerCase() == "th")
        {       
				noOfColumnHeders = parseInt(noOfColumnHeders) + parseInt(1);       	
				columnHeaderExists = true;
				
        }else{
			columnHeaderExists = false;
		}
		
		if(noOfColumnHeders > 0 && columnHeaderExists == false){
			rowStartIndex = rowIndex;
			break;
		}	
	}

	//alert("rowStartIndex"+rowStartIndex);
	
	if(rowStartIndex == -1)
	{
		var columStartIndex = findColumnsRowsWithoutTH(rows, columnname);

		//alert("columStartIndex :"+columStartIndex );

		rowStartIndex  = parseInt(columStartIndex) + parseInt(1)
	}
	
	//alert("rowStartIndex1:"+rowStartIndex);

	return rowStartIndex;
}





function findColumnsRowsWithoutTH(rows, columnName)
{
	var columnRow = -1;
	var rowLength = 0;
	var colLength = 0;

	if(rows.length > 4){
		rowLength = 4;
	}else{
		rowLength = rows.length;
	}

	for(var rowIndex = 0; rowIndex < rowLength; rowIndex++){
	
		var row = rows[rowIndex];

		var colLength = row.cells.length;
	
		if(row.cells.length > 4){
			colLength = 4;
		}else{
			colLength = row.cells.length;
		}

		for(var colIndex = 0; colIndex < colLength ; colIndex++){
		
			var column = row.cells[colIndex];

			var columnValue = column.innerText;

			//alert('Matched Column:'+column.innerText);

			if(column.innerText == columnName) {
				
				columnRow = rowIndex;
				break;
			}	
			
		}
		
		if(columnRow >= 0){
			break;
		}
		
	}

	return columnRow;

}

function findColumnsROW(rows)
{
	var columnRow = -1;
	var rowLength = 0;
	var colLength = 0;

	if(rows.length > 4){
		rowLength = 4;
	}else{
		rowLength = rows.length;
	}

	//alert("row Length :"+rowLength);

	for(var rowIndex = 0; rowIndex < rowLength; rowIndex++){
	
		var row = rows[rowIndex];

		//var colLength = row.cells.length;
	
		if(row.cells.length > 4){
			colLength = 4;
		}else{
			colLength = row.cells.length;
		}

		for(var colIndex = 0; colIndex < colLength ; colIndex++){

			if (row.cells[colIndex].tagName.toLowerCase() == "th")
            {
                columnRow = rowIndex;
				//alert("columnRow :"+columnRow );
				break;
            }
			
		}
		
		if(columnRow >= 0){
			break;
		}
		
	}

	return columnRow;

}


//Match the table by firstCell content.
//columnIndex are zero-based value.
//columnValue is the innerText of target table cell.
function getColumns(firstCell,tableIndex)
{
	
	var ret = new Array();
	
	/* Get Required Table */
	var table = getTable(firstCell ,tableIndex);
	
	/* Get Table Rows */ 	
	var rows = table.rows;
	//alert(rows.length);

	if(rows.length > 0) {
	
		//Get the Column Row Number
		var columnRow = findColumnsROW(rows);
		
		if(columnRow < 0){
			columnRow = findColumnsRowsWithoutTH(rows, firstCell);
		}
		
		if(columnRow >= 0){
			
			//Get Columns
			var row = rows[columnRow];
		
			var colLength = row.cells.length;


			var maxRowSpan = getMaxRowSpan(rows, columnRow);
			alert("maxRowSpan "+maxRowSpan +"  columnRow"+columnRow);


			var colCaptureInfo = new Array();
			
			for(int index=0; index < (maxRowSpan+columnRow); index++){
		
				var captureInfo =  0;
				colCaptureInfo.push(captureInfo);
			}

		
			var columns = getColumnsWithSpecificColumnSpan1(rows, columnRow, 0, colLength,colCaptureInfo);
			
			//alert("Columns :"+columns);
			ret = columns.split("~~");	
			
			// for(var colIndex = 0; colIndex < colLength; colIndex++)
			// {
				// var column = row.cells[colIndex];
				// var columnValue = column.innerText;
			
				// if(column.innerText == undefined || column.innerText == ''){
					// columnValue = 'empty';	
				// }
				//alert('Column value :'+columnValue);
	
				// ret.push(columnValue);
			// }
		}
		
	}	
	
	return ret;
}



function getMaxRowSpan(rows, columnRow){

	//Get Columns
	var row = rows[columnRow];
	
	var maxRowSpan = 1;
	
	for(var colIndex = startColIndex; colIndex < endColIndex; colIndex++)
	{
		var column = row.cells[colIndex];
		
		var rowSpan = column.getAttribute('rowspan');
		
		if(maxRowSpan <= rowSpan){
			maxRowSpan = rowSpan;
		}
		
	}

	return maxRowSpan;
}



function getColumnsWithSpecificColumnSpan1(rows, columnRow, startColIndex, endColIndex, colCaptureInfo){
	
	//Get Columns
	var row = rows[columnRow];
		
	var nextRowColIndex = 0;
	
	var columns = "";
	
	var maxRowSpan = getMaxRowSpan(rows, columnRow);
		
	for(var colIndex = startColIndex; colIndex < endColIndex; colIndex++)
	{
		var column = row.cells[colIndex];
		
		var rowSpan = column.getAttribute('rowspan');
		var colSpan = column.getAttribute('colspan');
		
		if(rowSpan == maxRowSpan){ //(rowSpan == 1 && colSpan == 1) 
				
			//alert("Column Span :"+colspan);
				
			var columnValue = column.innerText;
			
			if(column.innerText == undefined || column.innerText == ''){
				columnValue = 'empty';	
			}
			
			//alert('Column value :'+columnValue);
			
			if(columns == ""){
				columns = columnValue;
			}else{
				columns = columns + "~~" + columnValue;
			}
			
			
		}else if(rowSpan < maxRowSpan){
			
			// columnRow == Current Column row
			var rowNumber = columnRow + rowSpan;
			
			var nextRowColStartIndex = colCaptureInfo[rowNumber] ;
			var nextRowColEndIndex   = colCaptureInfo[rowNumber] + colSpan ;
			colCaptureInfo[rowNumber] = colSpan ; //colCaptureInfo[rowNumber] = 6 , means six elements are captured i.e when colSpan = 6 i.e 0 to 5 elements
			
			columns =  columns + "~~" + getColumnsWithSpecificColumnSpan(rows, rowNumber,nextRowColStartIndex,nextRowColEndIndex,colCaptureInfo);
		
		
		}
	}
	
	//alert("Columns in fun :"+columns);
	
	return columns;

}



function getColumnsWithSpecificColumnSpan(rows, columnRow, startColIndex, endColIndex){
	
	//Get Columns
	var row = rows[columnRow];
		
	var nextRowColIndex = 0;
	
	var columns = "";
			
	for(var colIndex = startColIndex; colIndex < endColIndex; colIndex++)
	{
		var column = row.cells[colIndex];
		
		var colSpan = column.getAttribute('colspan');
		
		if(colSpan == 1){
				
			//alert("Column Span :"+colspan);
				
			var columnValue = column.innerText;
			
			if(column.innerText == undefined || column.innerText == ''){
				columnValue = 'empty';	
			}
			
			//alert('Column value :'+columnValue);
			
			if(columns == ""){
				columns = columnValue;
			}else{
				columns = columns + "~~" + columnValue;
			}
			
			
		}else {
		
			var nextRowColStartIndex = nextRowColIndex;
			var nextRowColEndIndex = nextRowColIndex + colSpan ;
			
			if(columns == "" && colIndex == 0){ // first column is splitted to some columns
				columns = getColumnsWithSpecificColumnSpan(rows, columnRow+1,nextRowColStartIndex,nextRowColEndIndex);
			}else{
				columns = columns + "~~" + getColumnsWithSpecificColumnSpan(rows, columnRow+1,nextRowColStartIndex,nextRowColEndIndex);
			}

			
			// Provious code
			//columns = columns + "~~" + getColumnsWithSpecificColumnSpan(rows, columnRow+1,nextRowColStartIndex,nextRowColEndIndex);
			
			nextRowColIndex = nextRowColEndIndex;
		}
	}
	
	//alert("Columns in fun :"+columns);
	
	return columns;

}

//Match the table by firstCell content.
//columnIndex are zero-based value.
//columnValue is the innerText of target table cell.
function getRowCount(firstCell,tableIndex)
{
	

	var ret = new Array();
	
	//alert("firstCell :"+firstCell);

	/* Get Required Table */
	var table = getTable(firstCell ,tableIndex);
	
	/* Get Table Rows */ 	
	var rows = table.rows;
	
	//alert("rows :"+rows.length);
	
	return rows.length;
}

//Match the table by firstCell content.
//columnIndex are zero-based value.
//columnValue is the innerText of target table cell.
function getRowStartIndex(firstCell,tableIndex)
{
	var ret = new Array();
	
	/* Get Required Table */
	var table = getTable(firstCell ,tableIndex);
	
	/* Get Table Rows */ 	
	var rows = table.rows;
	
	var rowStartIndex = findRowIndex(rows, firstCell);
	
	return rowStartIndex;
}


