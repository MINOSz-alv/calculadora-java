package com.kramerlab;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/** KramerLab 3x3: resolución de sistemas lineales mediante la regla de Cramer. */
public final class KramerLab {
    private static final Color BG = new Color(14, 18, 32);
    private static final Color PANEL = new Color(23, 29, 48);
    private static final Color FIELD = new Color(12, 17, 31);
    private static final Color TEXT = new Color(235, 240, 255);
    private static final Color MUTED = new Color(157, 170, 198);
    private static final Color ACCENT = new Color(91, 141, 255);
    private static final Color GREEN = new Color(91, 220, 170);
    private static final Font BODY = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font MONO = new Font(Font.MONOSPACED, Font.PLAIN, 14);
    private final JTextField[][] a = new JTextField[3][3];
    private final JTextField[] b = new JTextField[3];
    private final JTextArea output = new JTextArea();
    private final JLabel status = new JLabel("Ingresa los coeficientes y pulsa Resolver sistema.");
    private final DecimalFormat df;

    private KramerLab() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        df = new DecimalFormat("0.####", symbols);
        df.setGroupingUsed(false);
        buildUi();
    }

    private void buildUi() {
        JFrame frame = new JFrame("KramerLab 3x3 | Regla de Cramer");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(820, 700));
        frame.setSize(980, 800);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 18));
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(24, 28, 24, 28));
        frame.setContentPane(root);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel eyebrow = new JLabel("MATEMÁTICAS · ÁLGEBRA LINEAL");
        eyebrow.setForeground(GREEN);
        eyebrow.setFont(new Font("SansSerif", Font.BOLD, 11));
        JLabel title = new JLabel("KramerLab 3x3");
        title.setForeground(TEXT);
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        JLabel subtitle = new JLabel("Resuelve sistemas de ecuaciones con la regla de Cramer");
        subtitle.setForeground(MUTED);
        subtitle.setFont(BODY);
        header.add(eyebrow); header.add(Box.createVerticalStrut(5)); header.add(title);
        header.add(Box.createVerticalStrut(5)); header.add(subtitle);
        root.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setOpaque(false);
        root.add(center, BorderLayout.CENTER);

        JPanel matrixPanel = panel();
        matrixPanel.setLayout(new BorderLayout(0, 14));
        JLabel matrixTitle = sectionTitle("Sistema de ecuaciones");
        matrixPanel.add(matrixTitle, BorderLayout.NORTH);
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 6, 5, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        String[] vars = {"x", "y", "z", "="};
        for (int col = 0; col < 4; col++) {
            c.gridx = col; c.gridy = 0;
            JLabel label = new JLabel(vars[col], SwingConstants.CENTER);
            label.setForeground(col == 3 ? GREEN : MUTED);
            label.setFont(new Font("SansSerif", Font.BOLD, 14));
            grid.add(label, c);
        }
        for (int row = 0; row < 3; row++) {
            c.gridy = row + 1;
            for (int col = 0; col < 4; col++) {
                c.gridx = col;
                JTextField field = styledField();
                field.setHorizontalAlignment(SwingConstants.CENTER);
                field.setFont(MONO);
                field.setText(col == row ? "1" : "0");
                field.setColumns(5);
                if (col < 3) a[row][col] = field; else b[row] = field;
                grid.add(field, c);
            }
        }
        matrixPanel.add(grid, BorderLayout.CENTER);
        JLabel format = new JLabel("Formato: ax + by + cz = d  ·  Acepta enteros y decimales.");
        format.setForeground(MUTED); format.setFont(new Font("SansSerif", Font.PLAIN, 12));
        matrixPanel.add(format, BorderLayout.SOUTH);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        controls.setOpaque(false);
        JButton solve = button("Resolver sistema", ACCENT, Color.WHITE);
        JButton example = button("Cargar ejemplo", new Color(41, 52, 78), TEXT);
        JButton clear = button("Limpiar", new Color(41, 52, 78), TEXT);
        controls.add(solve); controls.add(example); controls.add(clear);
        solve.addActionListener(e -> solveSystem());
        example.addActionListener(e -> loadExample());
        clear.addActionListener(e -> clearAll());

        JPanel resultPanel = panel();
        resultPanel.setLayout(new BorderLayout(0, 10));
        resultPanel.add(sectionTitle("Procedimiento y resultados"), BorderLayout.NORTH);
        output.setEditable(false);
        output.setLineWrap(true);
        output.setWrapStyleWord(false);
        output.setFont(MONO);
        output.setForeground(TEXT);
        output.setBackground(FIELD);
        output.setCaretColor(TEXT);
        output.setBorder(new EmptyBorder(14, 14, 14, 14));
        output.setText("Aquí aparecerá el procedimiento completo:\n\n"
                + "1. Determinante principal Δ = det(A).\n"
                + "2. Determinantes Δx, Δy y Δz.\n"
                + "3. Soluciones x = Δx/Δ, y = Δy/Δ, z = Δz/Δ.\n\n"
                + "Pulsa ‘Cargar ejemplo’ para probar el programa.");
        JScrollPane scroll = new JScrollPane(output);
        scroll.setBorder(new LineBorder(new Color(51, 62, 91), 1, true));
        scroll.getViewport().setBackground(FIELD);
        resultPanel.add(scroll, BorderLayout.CENTER);

        center.add(matrixPanel, BorderLayout.NORTH);
        JPanel lower = new JPanel(new BorderLayout(0, 12));
        lower.setOpaque(false);
        lower.add(controls, BorderLayout.NORTH);
        lower.add(resultPanel, BorderLayout.CENTER);
        center.add(lower, BorderLayout.CENTER);

        status.setForeground(MUTED); status.setFont(new Font("SansSerif", Font.PLAIN, 12));
        root.add(status, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private JPanel panel() {
        JPanel p = new JPanel();
        p.setBackground(PANEL);
        p.setBorder(new EmptyBorder(17, 18, 17, 18));
        return p;
    }

    private JLabel sectionTitle(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT); l.setFont(new Font("SansSerif", Font.BOLD, 16));
        return l;
    }

    private JTextField styledField() {
        JTextField f = new JTextField();
        f.setBackground(FIELD); f.setForeground(TEXT); f.setCaretColor(TEXT);
        f.setSelectionColor(ACCENT); f.setSelectedTextColor(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(62, 75, 109), 1, true),
                new EmptyBorder(8, 6, 8, 6)));
        return f;
    }

    private JButton button(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setForeground(fg); b.setBackground(bg); b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(11, 16, 11, 16));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void loadExample() {
        double[][] values = {{2, 1, -1}, {-3, -1, 2}, {-2, 1, 2}};
        double[] rhs = {8, -11, -3};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) a[i][j].setText(df.format(values[i][j]));
            b[i].setText(df.format(rhs[i]));
        }
        status.setText("Ejemplo cargado: solución esperada x = 2, y = 3, z = -1.");
        solveSystem();
    }

    private void clearAll() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) a[i][j].setText("");
            b[i].setText("");
        }
        output.setText("");
        status.setText("Campos limpiados.");
    }

    private void solveSystem() {
        try {
            double[][] matrix = new double[3][3];
            double[] rhs = new double[3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) matrix[i][j] = parse(a[i][j].getText(), "coeficiente fila " + (i + 1) + ", columna " + (j + 1));
                rhs[i] = parse(b[i].getText(), "término independiente fila " + (i + 1));
            }
            StringBuilder s = new StringBuilder();
            s.append("SISTEMA ORIGINAL\n");
            for (int i = 0; i < 3; i++) s.append(equation(matrix[i], rhs[i])).append('\n');
            double delta = det(matrix);
            s.append("\nPASO 1 · DETERMINANTE PRINCIPAL\nΔ = det(A) = ").append(fmt(delta)).append("\n");
            double[][] mx = replaceColumn(matrix, rhs, 0);
            double[][] my = replaceColumn(matrix, rhs, 1);
            double[][] mz = replaceColumn(matrix, rhs, 2);
            double dx = det(mx), dy = det(my), dz = det(mz);
            s.append("\nPASO 2 · DETERMINANTES MODIFICADOS\n");
            s.append("Δx = det(Ax) = ").append(fmt(dx)).append('\n');
            s.append("Δy = det(Ay) = ").append(fmt(dy)).append('\n');
            s.append("Δz = det(Az) = ").append(fmt(dz)).append('\n');
            s.append("\nPASO 3 · REGLA DE CRAMER\n");
            if (Math.abs(delta) < 1e-10) {
                s.append("Δ = 0, por lo tanto no existe una solución única.\n");
                s.append("El sistema puede ser incompatible o tener infinitas soluciones; se requiere analizarlo por otro método.\n");
                status.setText("El determinante principal es cero: no hay solución única por Cramer.");
            } else {
                double x = dx / delta, y = dy / delta, z = dz / delta;
                s.append("x = Δx / Δ = ").append(fmt(dx)).append(" / ").append(fmt(delta)).append(" = ").append(fmt(x)).append('\n');
                s.append("y = Δy / Δ = ").append(fmt(dy)).append(" / ").append(fmt(delta)).append(" = ").append(fmt(y)).append('\n');
                s.append("z = Δz / Δ = ").append(fmt(dz)).append(" / ").append(fmt(delta)).append(" = ").append(fmt(z)).append('\n');
                s.append("\nSOLUCIÓN FINAL\n(x, y, z) = (").append(fmt(x)).append(", ").append(fmt(y)).append(", ").append(fmt(z)).append(")\n");
                status.setText("Sistema resuelto correctamente.");
            }
            output.setText(s.toString());
            output.setCaretPosition(0);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Dato inválido", JOptionPane.WARNING_MESSAGE);
        }
    }

    private double parse(String raw, String label) {
        String text = raw.trim().replace(',', '.');
        if (text.isEmpty()) throw new NumberFormatException("Falta el valor de " + label + ".");
        try {
            double value = Double.parseDouble(text);
            if (!Double.isFinite(value)) throw new NumberFormatException();
            return value;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("El valor de " + label + " no es un número válido: '" + raw + "'.");
        }
    }

    private String equation(double[] row, double rhs) {
        StringBuilder s = new StringBuilder();
        String[] vars = {"x", "y", "z"};
        for (int j = 0; j < 3; j++) {
            if (j > 0) s.append(row[j] < 0 ? " - " : " + ");
            else if (row[j] < 0) s.append('-');
            s.append(fmt(Math.abs(row[j]))).append(vars[j]);
        }
        return s.append(" = ").append(fmt(rhs)).toString();
    }

    private double[][] replaceColumn(double[][] original, double[] rhs, int col) {
        double[][] copy = new double[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, 3);
            copy[i][col] = rhs[i];
        }
        return copy;
    }

    private double det(double[][] m) {
        return m[0][0] * (m[1][1] * m[2][2] - m[1][2] * m[2][1])
             - m[0][1] * (m[1][0] * m[2][2] - m[1][2] * m[2][0])
             + m[0][2] * (m[1][0] * m[2][1] - m[1][1] * m[2][0]);
    }

    private String fmt(double value) {
        if (Math.abs(value) < 1e-10) value = 0;
        return df.format(value);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
            catch (Exception ignored) { }
            new KramerLab();
        });
    }
}
