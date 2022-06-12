<?php
echo "starting";
if(isset($_GET["temp"])) {
    $temperature = $_GET["temp"]; // get temperature value from HTTP GET
    
    $servername = "localhost";
    $username = "ciprian";
    $password = "Ciprian2021.";
    $dbname = "arduweather";
    
    // Create connection
    $conn = new mysqli($servername, $username, $password, $dbname);
    // Check connection
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }
    
    //$sql = "INSERT INTO tbl_temp (temp_value) VALUES ($temperature)";
    
//     if ($conn->query($sql) === TRUE) {
//         echo "New record created successfully";
//     } else {
//         echo "Error: " . $sql . " => " . $conn->error;
//     }

    $sql = "select * from sensors";
    $result = mysql_query($sql);
    while($row = mysql_fetch_array($result)) {
        print_r($row);
        echo $row['idSensor'];
    }
    
    $conn->close();
} else {
    echo "temperature is not set";
}
?>
