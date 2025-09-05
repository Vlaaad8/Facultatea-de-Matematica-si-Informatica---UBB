<?php
    $server="localhost";
    $db="programareWeb";

    if($_SERVER['REQUEST_METHOD']!='GET'){
        die("Access Denied");
    }
    $connection=new mysqli($server,"root","",$db);

    if($connection->connect_error){
        die("Connection failed");
    }

    $output="";
    $sql="SELECT DISTINCT origin from rutecfr";
    $result=$connection->query($sql);
    if($result->num_rows>0){
        while($row=$result->fetch_assoc()){
            $origins=$row["origin"];
            $output.="<option value=\"$origins\">$origins</option>";
        }
    }
    $connection->close();
    echo $output;