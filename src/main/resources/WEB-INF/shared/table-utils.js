function createCell(){
  var row = document.createElement('tr');
  var table = document.getElementById("mediaTable");
  var row = table.insertRow(-1);
  row.innerHTML = "<th scope=\"row\"></th><td><input class=\"form-control\" type=\"number\"></td><td><input class=\"form-control\" type=\"text\"></td><td><input class=\"form-control\" type=\"text\"></td><td><select class=\"form-control\" type=\"text\"><option>PNG</option>    <option>JPEG</option>    <option>Youtube</option>    <option>SVG</option></select></td>";
}