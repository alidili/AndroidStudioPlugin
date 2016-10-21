package com.yang.dialog;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

/**
 * Created by yangle on 2016/10/19.
 */
public class LayoutCreator extends WriteCommandAction.Simple {

    private Project project;
    private PsiFile file;
    private PsiClass targetClass;
    private PsiElementFactory factory;

    public LayoutCreator(Project project, PsiClass targetClass, PsiElementFactory factory, PsiFile... files) {
        super(project, files);
        this.project = project;
        this.file = files[0];
        this.targetClass = targetClass;
        this.factory = factory;
    }

    @Override
    protected void run() throws Throwable {
        // 将弹出dialog的方法写在StringBuilder里
        StringBuilder dialog = new StringBuilder();
        dialog.append("public void showDialog(){");
        dialog.append("android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);");
        dialog.append("builder.setTitle(\"Title\")\n");
        dialog.append(".setMessage(\"Dialog content\")\n");
        dialog.append(".setPositiveButton(\"OK\", new android.content.DialogInterface.OnClickListener() {\n" +
                "@Override\n" +
                "public void onClick(DialogInterface dialog, int which) {\n" +
                "\t\n" +
                "}" +
                "})\n");
        dialog.append(".setNegativeButton(\"Cancel\", new DialogInterface.OnClickListener() {\n" +
                "@Override\n" +
                "public void onClick(DialogInterface dialog, int which) {\n" +
                "\t\n" +
                "}" +
                "})\n");
        dialog.append(".show();");
        dialog.append("}");

        // 将代码添加到当前类里
        targetClass.add(factory.createMethodFromText(dialog.toString(), targetClass));

        // 导入需要的类
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
        styleManager.optimizeImports(file);
        styleManager.shortenClassReferences(targetClass);
    }
}
