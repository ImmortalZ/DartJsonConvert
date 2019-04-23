package me.immortalz.dartjsonconvert.util;

import me.immortalz.dartjsonconvert.been.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateUtil {

    public static String generateFromJson(String str) throws Exception {
        String classSimpleName = getClassSimpleName(str);
        List<Variable> variableList = getVariableList(str);
        if (variableList == null || variableList.size() < 1) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("  ").append(classSimpleName).append(".fromJson(dynamic json)\n");
        int i = 0;
        for (Variable variable : variableList) {
            if (i == 0) {
                sb.append("      : ");
            } else {
                sb.append("        ");
            }
            sb.append(getJsonAssignment(variable));
            if (i == (variableList.size() - 1)) {
                sb.append(";");
            } else {
                sb.append(",");
            }
            sb.append("\n");
            i += 1;
        }
        return sb.toString();
    }

    private static String getClassSimpleName(String str) throws Exception {
        String name = RegexUtil.regexMatch(str, "class (.*) ");
        if (!DartTypeUtil.isObjectType(name)) {
            throw new Exception("class name do not conform to specifications ");
        }
        return name;
    }


    private static String getJsonAssignment(Variable variable) throws Exception {
        if (DartTypeUtil.isBasicVariableTyp(variable.type)) {
            return variable.name + " = json['" + variable.name + "']";
        } else if (DartTypeUtil.isListType(variable.type)) {
            return variable.name + " = " + listTypeParse(variable);
        } else if (DartTypeUtil.isObjectType(variable.type)) {
            return variable.name + " = " + variable.type + ".fromJson(json['" + variable.name + "'])";
        }
        throw new Exception("dart type is error! please check it");
    }

    private static String listTypeParse(Variable variable) throws Exception {
        String listType = DartTypeUtil.getListDynamicType(variable.type);
        if (DartTypeUtil.isBasicVariableTyp(listType)) {
            if (variable.iterator) {
                return "new List<" + listType + ">.from(" + variable.name + ")";
            }
            return "new List<" + listType + ">.from(json['" + variable.name + "'])";
        } else if (DartTypeUtil.isObjectType(listType)) {
            if (variable.iterator) {
                return "(" + variable.name + " as List).map((i) => " + listType + ".fromJson(i)).toList()";
            }
            return "(json['" + variable.name + "'] as List).map((i) => " + listType + ".fromJson(i)).toList()";
        } else if (DartTypeUtil.isListType(listType)) {
            return "(json['" + variable.name + "'] as List).map((i) => " +
                    listTypeParse(new Variable(listType, "i", true)) +
                    ").toList()";
        }
        throw new Exception("no support List type, please check List<dynamic> type");
    }

    private static List<Variable> getVariableList(String str) {
        List<Variable> result = new ArrayList<>();
        Matcher typeMatcher = Pattern.compile(".*?(\\S+).*;").matcher(str);
        while (typeMatcher.find()) {
            if (!typeMatcher.group(0).contains("//")) {
                // type
                String type = typeMatcher.group(1);
                String name = null;
                // name
                Matcher keyMatcher = Pattern.compile("\\s+(\\S+);$").matcher(typeMatcher.group(0));
                if (keyMatcher.find()) {
                    name = keyMatcher.group(1);
                }
                //
                Variable v = new Variable(type, name);
                result.add(v);
            }
        }
        return result;
    }

}
