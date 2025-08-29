package Golf.Entity;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GolfVisualizer extends JFrame {
    private final GolfMap map;
    private final List<String> agentPath;
    private int currentStep = 0;
    private Timer animationTimer;
    private JLabel[][] gridLabels;
    private JLabel statusLabel;
    private JButton startButton, stopButton;
    private boolean isRunning = false;
    private JLabel ballLabel;
    private JLayeredPane layeredPane;
    private JPanel gridPanel;
    private JScrollPane scrollPane;
    private int minCellSize = 15;
    private int maxCellSize = 50;
    private int cellSize;

    public GolfVisualizer(GolfMap map, List<String> agentPath) {
        this.map = map;
        this.agentPath = agentPath;
        calculateOptimalCellSize();
        initializeUI();
    }

    private void calculateOptimalCellSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int availableWidth = (int)(screenSize.width * 0.8);
        int availableHeight = (int)(screenSize.height * 0.7);

        calculateCellSize(availableWidth, availableHeight);
    }

    private void calculateCellSize(int availableWidth, int availableHeight) {
        int widthBasedSize = availableWidth / map.getWidth();
        int heightBasedSize = availableHeight / map.getHeight();

        cellSize = Math.min(widthBasedSize, heightBasedSize);
        cellSize = Math.max(cellSize, minCellSize);
        cellSize = Math.min(cellSize, maxCellSize);
    }

    private void initializeUI() {
        setTitle("Golf Course Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Главный контейнер
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Создаем панель с сеткой
        createGridPanel();

        // Создаем слоистую панель
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        updateLayeredPaneSize();
        layeredPane.add(gridPanel, JLayeredPane.DEFAULT_LAYER);

        // Создаем мяч
        ballLabel = new JLabel("●", SwingConstants.CENTER);
        updateBallAppearance();
        layeredPane.add(ballLabel, JLayeredPane.PALETTE_LAYER);

        // Добавляем скроллинг
        scrollPane = new JScrollPane(layeredPane);
        updateScrollPaneSize();

        // Панель управления
        JPanel controlPanel = createControlPanel();

        // Компоновка главного окна
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // Обработчик изменения размера окна
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustSizesToWindow();
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        if (!agentPath.isEmpty()) {
            updateBallPosition(currentStep);
        }
    }

    private void createGridPanel() {
        gridPanel = new JPanel(new GridLayout(map.getHeight(), map.getWidth(), 0, 0));
        gridLabels = new JLabel[map.getHeight()][map.getWidth()];

        for (int h = 0; h < map.getHeight(); h++) {
            for (int w = 0; w < map.getWidth(); w++) {
                JLabel cell = new JLabel("", SwingConstants.CENTER);
                cell.setOpaque(true);
                cell.setBackground(getCellColor(map.getCell(h, w)));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cell.setPreferredSize(new Dimension(cellSize, cellSize));
                gridPanel.add(cell);
                gridLabels[h][w] = cell;
            }
        }
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        startButton = new JButton("Старт");
        startButton.addActionListener(this::startAnimation);

        stopButton = new JButton("Стоп");
        stopButton.addActionListener(this::stopAnimation);
        stopButton.setEnabled(false);

        JButton stepButton = new JButton("Шаг");
        stepButton.addActionListener(this::nextStep);

        statusLabel = new JLabel("Готово");

        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        controlPanel.add(stepButton);
        controlPanel.add(statusLabel);

        return controlPanel;
    }

    private void adjustSizesToWindow() {
        // Получаем текущие размеры окна (минус панель управления)
        int availableWidth = getWidth() - 20;
        int availableHeight = getHeight() - 70; // 70 - примерная высота панели управления

        // Пересчитываем размер клеток
        calculateCellSize(availableWidth, availableHeight);

        // Обновляем все компоненты
        updateGridCells();
        updateLayeredPaneSize();
        updateScrollPaneSize();
        updateBallAppearance();
        updateBallPosition(currentStep);

        revalidate();
        repaint();
    }

    private void updateGridCells() {
        for (int h = 0; h < map.getHeight(); h++) {
            for (int w = 0; w < map.getWidth(); w++) {
                gridLabels[h][w].setPreferredSize(new Dimension(cellSize, cellSize));
            }
        }
        gridPanel.revalidate();
    }

    private void updateLayeredPaneSize() {
        layeredPane.setPreferredSize(
                new Dimension(
                        map.getWidth() * cellSize,
                        map.getHeight() * cellSize
                )
        );
        gridPanel.setBounds(0, 0, map.getWidth() * cellSize, map.getHeight() * cellSize);
    }

    private void updateScrollPaneSize() {
        scrollPane.setPreferredSize(new Dimension(
                getWidth() - 20,
                getHeight() - 70
        ));
    }

    private void updateBallAppearance() {
        ballLabel.setFont(new Font("Arial", Font.BOLD, cellSize/2));
        ballLabel.setSize(cellSize, cellSize);
    }

    private void updateBallPosition(int step) {
        if (agentPath == null || agentPath.isEmpty()) return;

        String[] parts = agentPath.get(step).split(",");
        int h = Integer.parseInt(parts[0]);
        int w = Integer.parseInt(parts[1]);

        int x = w * cellSize;
        int y = h * cellSize;

        ballLabel.setLocation(x, y);
        layeredPane.repaint();
    }

    // Остальные методы без изменений
    private void startAnimation(ActionEvent e) {
        if (animationTimer == null) {
            animationTimer = new Timer(500, evt -> {
                if (currentStep < agentPath.size() - 1) {
                    currentStep++;
                    updateBallPosition(currentStep);
                    highlightCurrentCell();
                    statusLabel.setText("Шаг " + currentStep + " из " + (agentPath.size()-1));
                } else {
                    stopAnimation(null);
                    JOptionPane.showMessageDialog(this, "Маршрут завершен!");
                }
            });
        }

        animationTimer.start();
        isRunning = true;
        updateControls();
        statusLabel.setText("Идет анимация...");
    }

    private void stopAnimation(ActionEvent e) {
        if (animationTimer != null) {
            animationTimer.stop();
        }
        isRunning = false;
        updateControls();
        statusLabel.setText("Остановлено");
    }

    private void nextStep(ActionEvent e) {
        if (currentStep < agentPath.size() - 1) {
            currentStep++;
            updateBallPosition(currentStep);
            highlightCurrentCell();
            statusLabel.setText("Шаг " + currentStep + " из " + (agentPath.size()-1));
        } else {
            JOptionPane.showMessageDialog(this, "Маршрут завершен!");
        }
    }

    private void highlightCurrentCell() {
        for (int h = 0; h < map.getHeight(); h++) {
            for (int w = 0; w < map.getWidth(); w++) {
                gridLabels[h][w].setBackground(getCellColor(map.getCell(h, w)));
            }
        }

        String[] parts = agentPath.get(currentStep).split(",");
        int h = Integer.parseInt(parts[0]);
        int w = Integer.parseInt(parts[1]);
        gridLabels[h][w].setBackground(new Color(173, 216, 230));
    }

    private void updateControls() {
        startButton.setEnabled(!isRunning);
        stopButton.setEnabled(isRunning);
    }

    private Color getCellColor(int cellType) {
        return switch (cellType) {
            case 1 -> new Color(194, 178, 128); // песок
            case 2 -> new Color(0, 100, 0);    // грин
            case 3 -> new Color(255, 0, 0);     // лунка
            default -> new Color(34, 139, 34);   // трава
        };
    }
}