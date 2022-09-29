package github.kasuminova.balloonserver.GUI;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkContrastIJTheme;
import github.kasuminova.balloonserver.GUI.Panels.AboutPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.InputStream;
import java.util.Enumeration;

/**
 * @author Kasumi_Nova
 * 一个工具类，用于初始化 Swing（即美化）
 */
public class SetupSwing {
    public static void init() {
        long start1 = System.currentTimeMillis();
        //抗锯齿字体
        System.setProperty("awt.useSystemAAFontSettings", "lcd");
        System.setProperty("swing.aatext", "true");

        //UI 配置线程
        Thread uiThread = new Thread(() -> {
            long start = System.currentTimeMillis();
            //设置圆角弧度
            UIManager.put("Button.arc", 7);
            UIManager.put("Component.arc", 7);
            UIManager.put("ProgressBar.arc", 7);
            UIManager.put("TextComponent.arc", 5);
            UIManager.put("CheckBox.arc", 3);
            //设置滚动条
            UIManager.put("ScrollBar.showButtons", false);
            UIManager.put("ScrollBar.thumbArc", 7);
            UIManager.put("ScrollBar.width", 12);
            UIManager.put("ScrollBar.thumbInsets", new Insets(2,2,2,2));
            UIManager.put("ScrollBar.track", new Color(0,0,0,0));
            //选项卡分隔线
            UIManager.put("TabbedPane.showTabSeparators", true);
            UIManager.put("TabbedPane.tabSeparatorsFullHeight", true);
            //菜单
            UIManager.put("MenuItem.selectionType", "underline");
            UIManager.put("MenuItem.underlineSelectionHeight", 3);
            UIManager.put("MenuItem.margin", new Insets(5,8,3,5));
            UIManager.put("MenuBar.underlineSelectionHeight", 3);
            //窗口标题居中
            UIManager.put("TitlePane.centerTitle", true);
            UIManager.put("TitlePane.centerIcon", true);
            //进度条
            UIManager.put("ProgressBar.repaintInterval", 16);
            UIManager.put("ProgressBar.cycleTime", 7500);
            //提示框
            UIManager.put("ToolTip.border", new LineBorder(new Color(55,60,70), 2, true));

            System.out.printf("UIThread Completed, Used %sms%n", System.currentTimeMillis() - start);
        });
        uiThread.start();

        //字体更换线程
        Thread fontThread = new Thread(() -> {
            long start = System.currentTimeMillis();
            //设置字体
            try {
                InputStream sarasa_mono = SetupSwing.class.getResourceAsStream("/font/sarasa-mono-sc-regular.ttf");
                InputStream saira = AboutPanel.class.getResourceAsStream("/font/Saira-Medium.ttf");
                Font font;
                if (sarasa_mono != null) {
                    font = Font.createFont(Font.TRUETYPE_FONT, sarasa_mono).deriveFont(13f);
                    initGlobalFont(font);
                }
                if (saira != null) {
                    font = Font.createFont(Font.TRUETYPE_FONT, saira).deriveFont(15f);
                    UIManager.put("TitlePane.font", font);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.printf("FontThread Completed, Used %sms%n", System.currentTimeMillis() - start);
        });
        fontThread.start();

        Thread themeThread = new Thread(() -> {
            long start = System.currentTimeMillis();
            //更新 UI
            try {
                UIManager.setLookAndFeel(new FlatAtomOneDarkContrastIJTheme());
            } catch (Exception e) {
                System.err.println("Failed to initialize LaF");
                e.printStackTrace();
            }
            System.out.printf("ThemeThread Completed, Used %sms%n", System.currentTimeMillis() - start);
        });
        themeThread.start();

        try {
            uiThread.join();
            fontThread.join();
            themeThread.join();
        } catch (InterruptedException ignored) {}

        System.out.printf("SetupSwing Completed, Used %sms%n", System.currentTimeMillis() - start1);
    }

    /**
     * 载入全局字体
     * @param font 字体
     */
    private static void initGlobalFont(Font font) {
        FontUIResource fontResource = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontResource);
            }
        }
    }
}