package ru.itis2016;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

public class CalculatorServlet extends HttpServlet {

    private static String writeHtml(String str) {
        return "<!DOCTYPE html>\n"
                + "<html lang=\"ru\">\n"
                + "<head>\n"
                + "<title>Калькулятор</title>\n"
                + "<meta charset=\"utf-8\">"
                + "</head>\n"
                + "<body>\n"
                + "<style>"
                + ".last {padding: 10px; border: 1px dashed #ccc; width: 100%; box-sizing: border-box;}"
                + ".digit {padding: 10px; box-sizing: border-box; width: 100%;}"
                + ".calculator {max-width: 250px}"
                + ".calculate {display: block; width: 100%; padding: 10px 20px;}"
                + ".oper {width: 40px; height: 40px; float: left; margin-right:20px;}"
                + ".Maths {width: 99%; margin: 0 auto; margin-bottom: 20px; clear: both;}"
                + ".Maths:before, .Maths:after {display: table; content:\" \";clear: both;}"
                + "</style>"
                + str
                + "</body>\n"
                + "</html>";
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        HttpSession session = request.getSession();
        //обнуление сессионных полей
        session.setAttribute("digit", null);
        session.setAttribute("oper", null);
        PrintWriter print = response.getWriter();
        String code = "<h1>Супер калькулятор v1.0</h1>"
                + "<p>Введите значение:</p>"
                +"<form method=\"post\" class=\"calculator\" action=\"/calculator\">\n"
                +"<input class=\"digit\" type=\"text\" autocomplete=\"off\" name=\"digit\">\n"
                +"<p class=\"last\">"+null+"</p>\n"
                +"<p class=\"last\">"+null+"</p>\n"
                +"<p>Выберите действие:</p>"
                +"<div class=\"Maths\">\n"
                +"<input class=\"oper\" type=\"submit\" name=\"mathaction\" value=\"+\">\n"
                +"<input class=\"oper\" type=\"submit\" name=\"mathaction\" value=\"-\">\n"
                +"<input class=\"oper\" type=\"submit\" name=\"mathaction\" value=\"*\">\n"
                +"<input class=\"oper\" type=\"submit\" name=\"mathaction\" value=\"/\">\n"
                +"</div>\n"
                +"<input class=\"calculate\" type=\"submit\" name=\"mathaction\" value=\"=\">\n";
        print.write(writeHtml(code));

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        String digit = request.getParameter("digit");
        String oper = request.getParameter("mathaction");
        HttpSession ses = request.getSession();
        String ssDigit = (String) ses.getAttribute("digit");
        String ssOper = (String) ses.getAttribute("oper");
        boolean numberError = false;
        if(digit.equals("")) {
            ses.setAttribute("digit", null);
            ses.setAttribute("oper", null);
            ssDigit = "";
            ssOper = "";
        } else {
            try {
                if(digit.contains(",")) {
                    digit = digit.replace(",",".");
                }
                Double check = Double.valueOf(digit);
            } catch (NumberFormatException nfexc) {
                numberError = true;
                ssDigit = "<span style=\"color: red\">Допустимы только числа!</span>";
                ses.setAttribute("digit", null);
                ses.setAttribute("oper", null);
            }
            if(ssDigit == null && ssOper == null && !numberError) {
                ses.setAttribute("digit", digit);
                ses.setAttribute("oper", oper);
                ssDigit = (String) ses.getAttribute("digit");
                ssOper = (String) ses.getAttribute("oper");
            } else if(!numberError) {
                String digitTemp = null;
                boolean error = false;
                if(ssOper.equals("+")) {
                    digitTemp = String.valueOf(Double.valueOf(ssDigit) + Double.valueOf(digit));
                } else if(ssOper.equals("-")) {
                    digitTemp = String.valueOf(Double.valueOf(ssDigit) - Double.valueOf(digit));
                } else if(ssOper.equals("*")) {
                    digitTemp = String.valueOf(Double.valueOf(ssDigit) * Double.valueOf(digit));
                } else if(ssOper.equals("/")) {
                    try {
                        digitTemp = String.valueOf(Double.valueOf(ssDigit) / Double.valueOf(digit));
                        if (Double.valueOf(digitTemp) == Double.POSITIVE_INFINITY ||
                                Double.valueOf(digitTemp) == Double.NEGATIVE_INFINITY)
                            throw new ArithmeticException();
                    } catch (ArithmeticException aexc) {
                        digitTemp = "<span style=\"color:red\">Недопустимое арифметическое действие!</span>";
                        error = true;
                        ses.setAttribute("digit", null);
                        ses.setAttribute("oper", null);
                    }
                } else if(ssOper.equals("=")) {
                    digitTemp = ssDigit;
                }
                ses.setAttribute("digit", digitTemp);
                ses.setAttribute("oper", oper);
                ssDigit = (String) ses.getAttribute("digit");
                ssOper = (String) ses.getAttribute("oper");
                if(ssOper.equals("=") || error) {
                    ses.setAttribute("digit", null);
                    ses.setAttribute("oper", null);
                }
            }
        }

        PrintWriter print = response.getWriter();

        String code = "<h1>Супер калькулятор v1.0</h1>"
                +"<p>Введите значение:</p>"
                +"<form method=\"post\" class=\"calculator\" action=\"/calculator\">\n"
                +"<input class=\"digit\" type=\"text\" autocomplete=\"off\" name=\"digit\">\n"
                +"<p class=\"last\">"+ssDigit+"</p>\n"
                +"<p class=\"last\">"+ssOper+"</p>\n"
                +"<p>Выберите действие:</p>"
                +"<div class=\"Maths\">\n"
                +"<input class=\"oper\" type=\"submit\" name=\"mathaction\" value=\"+\">\n"
                +"<input class=\"oper\" type=\"submit\" name=\"mathaction\" value=\"-\">\n"
                +"<input class=\"oper\" type=\"submit\" name=\"mathaction\" value=\"*\">\n"
                +"<input class=\"oper\" type=\"submit\" name=\"mathaction\" value=\"/\">\n"
                +"</div>\n"
                +"<input class=\"calculate\" type=\"submit\" name=\"mathaction\" value=\"=\">\n";
        print.write(writeHtml(code));

    }

}
