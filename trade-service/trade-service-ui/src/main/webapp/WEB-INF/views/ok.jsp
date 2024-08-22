<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page isELIgnored = "false" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Title</title>
</head>
<body>
<div>
    <form action="/anastasia/smart/subscribe" method="POST">
        <label id="account">account
            <input type="text" name="account" value="Finam:462932R56XU">
        </label>
        <br>
        <br>
        <label>time_frame
            <select name="time_frame">
                <option value="D1">D1</option>
                <option value="H1">H1</option>
            </select>
        </label>
        <br>
        <br>
        <label>strategy
            <input type="text" name="strategy" value="MovingAverageStrategy">
        </label>
        <br>
        <br>
        <button type="submit">Ok</button>
    </form>
</div>
<div>
    <form action="/anastasia/smart/unsubscribe" method="POST">
        <label>account
            <input type="text" name="account" value="Finam:462932R56XU">
        </label>
        <br>
        <br>
        <label>time_frame
            <select name="time_frame">
                <option value="D1">D1</option>
                <option value="H1">H1</option>
            </select>
        </label>
        <br>
        <br>
        <label>strategy
            <input type="text" name="strategy" value="MovingAverageStrategy">
        </label>
        <br>
        <br>
        <button type="submit">Ok</button>
    </form>
</div>
</body>
</html>