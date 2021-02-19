import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SpaceShooting extends JFrame {
    final int windowWidth = 800;
    final int windowHeight = 800;
    public static boolean isBattle = false;	//疑似グローバル変数

    public static void main(String[] args){
        new SpaceShooting();
    }

    /* フレーム設定 */
    public SpaceShooting() {
        Dimension dimOfScreen = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(dimOfScreen.width/2 - windowWidth/2, dimOfScreen.height/2 - windowHeight/2, windowWidth, windowHeight);
        setResizable(false);
        setTitle("スペースシューティング");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
	panelCange();
    }

    /* パネル切り替え */
    public void panelCange() {
	// タイトルから戦闘画面へ
	if (!isBattle) {
	    TitleJPanel title = new TitleJPanel();
            setBackground(Color.black);
	    Container t = getContentPane();
            t.add(title);
            setVisible(true);
	    requestFocus();
	} else {
	    getContentPane().removeAll();
	    repaint();
	    MyJPanel panel = new MyJPanel();
            Container c = getContentPane();
            c.add(panel);
            setVisible(true);
	    panel.requestFocus();
	}
    }

    /* タイトル画面 */
    public class TitleJPanel extends MyJPanel implements ActionListener {
	/* タイトル画面に関する変数 */
	JButton startButton, barrierButton;
	JLabel titleLabel, explanationLabel1, explanationLabel2, explanationLabel3;
	boolean barrierDetail = false;		// バリア詳細表示かどうかを判断する

	/* コンストラクタ（ゲーム開始時の初期化）*/
	public TitleJPanel() {
	    setLayout(null);

	    // ラベルの作成
	    titleLabel = new JLabel("スペースシューティング");
	    explanationLabel1 = new JLabel("操 作 説 明");
	    explanationLabel2 = new JLabel("<html>左クリック：ミサイル発射<br/>右クリック：バリア<br/>E&thinsp;s&thinsp;cキー&emsp;：ポーズメニュー</html>");
	    explanationLabel3 = new JLabel("<html>&emsp;&emsp;&emsp;バリアで敵の猛攻をやり過ごそう！<br/>&emsp;&emsp;射撃後しばらくはバリアが張れないよ！<br/>バリア中は動けないし、弾も撃てないので注意！</html>");

	    titleLabel.setForeground(Color.white);
	    explanationLabel1.setForeground(Color.white);
	    explanationLabel2.setForeground(Color.white);
	    explanationLabel3.setForeground(Color.white);

	    titleLabel.setHorizontalAlignment(titleLabel.CENTER);
	    explanationLabel1.setHorizontalAlignment(explanationLabel1.CENTER);
	    explanationLabel2.setHorizontalAlignment(explanationLabel2.CENTER);
	    explanationLabel3.setHorizontalAlignment(explanationLabel3.CENTER);

	    titleLabel.setFont(new Font("HG正楷書体-PRO", Font.PLAIN, 62));
	    explanationLabel1.setFont(new Font("HG正楷書体-PRO", Font.PLAIN, 35));
	    explanationLabel2.setFont(new Font("HG正楷書体-PRO", Font.PLAIN, 30));
	    explanationLabel3.setFont(new Font("HG正楷書体-PRO", Font.PLAIN, 30));

	    titleLabel.setBounds(0, 0 - 200, windowWidth, windowHeight);
	    explanationLabel1.setBounds(0, 0 - 100, windowWidth, windowHeight);
	    explanationLabel2.setBounds(0, 0, windowWidth, windowHeight);
	    explanationLabel3.setBounds(0, 0 + 120, windowWidth, windowHeight);

	    add(titleLabel);
	    add(explanationLabel1);
	    add(explanationLabel2);
	    add(explanationLabel3);

	    // ボタンの作成
            startButton = new JButton("ゲームスタート");
	    startButton.addActionListener(this);
	    startButton.setForeground(Color.white);
            startButton.setBackground(new Color(70, 70, 150));
	    startButton.setHorizontalAlignment(startButton.CENTER);
	    startButton.setFont(new Font("HG正楷書体-PRO", Font.PLAIN, 50));
	    startButton.setBounds(100, windowHeight - 150, windowWidth - 200, 70);
	    add(startButton);
	}

	/* パネル上の描画 */
	public void paintComponent(Graphics g) {
	    g.setColor(new Color(60, 60, 60));
	    g.fillRect(0, 0 + 120, windowWidth, windowHeight - 300);
	}

	/* 一定時間ごとの処理（ActionListener に対する処理）*/
        public void actionPerformed(ActionEvent e) {
	    //スタートボタンが押されたらパネルチェンジメソッドへ
	    if (e.getSource() == startButton) {
		SpaceShooting.isBattle = true;
		panelCange();
	        repaint();
	    }
        }
    }

    /* 戦闘画面 */
    public class MyJPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {

        /* 全体の設定に関する変数 */
        Dimension dimOfPanel;
        Timer timer;
        ImageIcon iconMe, iconEnemy;
        Image imgMe, imgEnemy;
        int FrameRate = 1000/120;
	int Pause = 0;

	/* ゲーム終了に関する変数*/
        JButton exit;
	boolean isGameSet = false;
	int result = 0;
	JLabel winLabel, loseLabel, pTextLabel1, pTextLabel2;

        /* 自機に関する変数 */
        int myWidth, myHeight;
        int myX, myY, tempMyX;
        int gap = 100;
        int myMissileX, myMissileY;
	int barrierWidth = 70, barrierHeight = 6;
	int barrierX, barrierY;
        boolean isMyMissileActive;
	boolean isRightClick = false;

        /* 敵機に関する変数 */
        int numOfEnemy = 12;
        int numOfAlive = numOfEnemy;
        int enemyWidth, enemyHeight;
        int[] enemyX = new int[numOfEnemy];
        int[] enemyY = new int[numOfEnemy];
        int[] enemyMove = new int[numOfEnemy];
        int[] enemyMissileX = new int[numOfEnemy];
        int[] enemyMissileY = new int[numOfEnemy];
        int[] enemyMissileSpeed = new int[numOfEnemy];
        boolean[] isEnemyAlive = new boolean[numOfEnemy];
        boolean[] isEnemyMissileActive = new boolean[numOfEnemy];

        /* コンストラクタ（ゲーム開始時の初期化）*/
        public MyJPanel() {
            // 全体の設定
            setBackground(Color.black);
            addMouseListener(this);
            addMouseMotionListener(this);
	    addKeyListener(this);
            timer = new Timer(FrameRate, this);
            timer.start();

            // 画像の取り込み
            imgMe = getImg("jiki.jpg");
            myWidth = imgMe.getWidth(this);
            myHeight = imgMe.getHeight(this);

            imgEnemy = getImg("teki.jpg");
            enemyWidth = imgEnemy.getWidth(this);
            enemyHeight = imgEnemy.getHeight(this);

            // 自機と敵機の初期化
            initMyPlane();
            initEnemyPlane();
        }

        /* パネル上の描画 */
        public void paintComponent(Graphics g) {
            dimOfPanel = getSize();
            super.paintComponent(g);

            // 各要素の描画
            drawMyPlane(g);       // 自機
            drawMyMissile(g);     // 自機のミサイル
            drawEnemyPlane(g);    // 敵機
            drawEnemyMissile(g);  // 敵機のミサイル
	    rightClick(g);	  // バリア

	    // ラベルの作成
	    winLabel = new JLabel("ゲームクリア！");
	    loseLabel = new JLabel("ゲームオーバー");
	    pTextLabel1 = new JLabel("～ポーズ中～");
	    pTextLabel2 = new JLabel("Escキーを押すと再開");
	    winLabel.setForeground(Color.white);
	    loseLabel.setForeground(Color.white);
	    pTextLabel1.setForeground(Color.white);
	    pTextLabel2.setForeground(Color.white);
	    winLabel.setHorizontalAlignment(winLabel.CENTER);
	    loseLabel.setHorizontalAlignment(loseLabel.CENTER);
	    pTextLabel1.setHorizontalAlignment(pTextLabel1.CENTER);
	    pTextLabel2.setHorizontalAlignment(pTextLabel2.CENTER);
	    winLabel.setFont(new Font("HG正楷書体-PRO", Font.PLAIN, 80));
	    loseLabel.setFont(new Font("HG正楷書体-PRO", Font.PLAIN, 70));
	    pTextLabel1.setFont(new Font("HG正楷書体-PRO", Font.PLAIN, 80));
	    pTextLabel2.setFont(new Font("HG正楷書体-PRO", Font.PLAIN, 38));
	    winLabel.setBounds(0, 0 - 50, windowWidth, windowHeight);
	    loseLabel.setBounds(0, 0 - 50, windowWidth, windowHeight);
	    pTextLabel1.setBounds(0, 0 - 50, windowWidth, windowHeight);
	    pTextLabel2.setBounds(0, 0 + 50, windowWidth, windowHeight);

	    // ボタンの作成
	    exit = new JButton("終了");
	    exit.addActionListener(this);
	    exit.setFont(new Font("HG正楷書体-PRO", Font.PLAIN, 20));
	    exit.setBackground(Color.white);
	    exit.setBounds(windowWidth - 100, windowHeight - 100, 80, 30);

            // 敵機を全機撃墜した時の終了処理
            if (numOfAlive == 0) {
		timer.stop();
                isGameSet = true;
		result = 1;
            }

	    // ゲーム終了ボタンの描画
	    if (isGameSet) {
	        setLayout(null);
                add(exit);
	    }

	    // ゲーム終了ラベルの描画
	    if (result == 1) {
		add(winLabel);
	    } else if (result == 2) {
		add(loseLabel);
	    }

	    // ポーズ画面切り替えの描画
	    if(Pause == 1) {
		Pause = 2;
		add(pTextLabel1);
		add(pTextLabel2);
		setBackground(new Color(40, 40, 40, 200));
		timer.stop();
	    } else if(Pause == 3){
		Pause = 0;
		removeAll();
		setBackground(Color.black);
		timer.start();
	    }
        }

	/* KeyListener に対する処理 */
	// キーボタンを押下する
	public void keyPressed(KeyEvent e) {
	    // Esc キーが押されたら画面ストップ、もう一度押すともとに戻る
	    if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !isGameSet && isBattle && Pause == 0) {
		Pause = 1;
	    } else if(e.getKeyCode() == KeyEvent.VK_ESCAPE && Pause == 2) {
		Pause = 3;
		repaint();
	    }
	}

	// キーボタンを離す
	public void keyReleased(KeyEvent e) {
	}

	// キーボタンを入力する
	public void keyTyped(KeyEvent e) {
	}

        /* 一定時間ごとの処理（ActionListener に対する処理）*/
	// 終了設定
        public void actionPerformed(ActionEvent e) {
	    if (e.getSource() == exit) {
		System.exit(0);
	    }
            repaint();
        }

        /* MouseListener に対する処理 */
        // マウスボタンをクリックする
        public void mouseClicked(MouseEvent e) {
        }

        // マウスボタンを押下する
        public void mousePressed(MouseEvent e) {
	    // 左クリック・右クリック判定
	    if (e.getButton() == MouseEvent.BUTTON1){
		// 左クリックなら射撃
		if (!isMyMissileActive && !isRightClick) {
		    myMissileX = tempMyX + myWidth/2;
                    myMissileY = myY;
                    isMyMissileActive = true;
		}
	    // 右クリックならバリア
	    }else if (e.getButton() == MouseEvent.BUTTON3){
		if (!isMyMissileActive) {
		    isRightClick = true;
		}
	    }
        }

	//右クリックが押されたらバリアを描画する
	public void rightClick(Graphics g) {
	    barrierX = tempMyX - 20;
	    barrierY = myY - 20;
	    if (isRightClick) {
		g.setColor(Color.blue);
		g.fillRect(barrierX, barrierY, barrierWidth, barrierHeight);
	    }
	}

        // マウスボタンを離す
        public void mouseReleased(MouseEvent e) {
	    if (e.getButton() == MouseEvent.BUTTON3){
		isRightClick = false;
	    }
        }

        // マウスが領域外へ出る
        public void mouseExited(MouseEvent e) {
        }

        // マウスが領域内に入る
        public void mouseEntered(MouseEvent e) {
        }

        /* MouseMotionListener に対する処理 */
        // マウスを動かす
        public void mouseMoved(MouseEvent e) {
	    if (!isRightClick) {
                myX = e.getX();
	    }
        }

        // マウスをドラッグする
        public void mouseDragged(MouseEvent e) {
	    if (!isRightClick) {
                myX = e.getX();
	    }
        }

        /* 画像ファイルから Image クラスへの変換 */
        public Image getImg(String filename) {
            ImageIcon icon = new ImageIcon(filename);
            Image img = icon.getImage();

            return img;
        }

        /* 自機の初期化 */
        public void initMyPlane() {
            myX = windowWidth / 2;
            myY = windowHeight - 100;
            tempMyX = windowWidth / 2;
            isMyMissileActive = false;
        }

        /* 敵機の初期化 */
        public void initEnemyPlane() {
            for (int i=0; i<7; i++) {
                enemyX[i] = 70*i;
                enemyY[i] = 100;
            }

            for (int i=7; i<numOfEnemy; i++) {
                enemyX[i] = 70*(i-6);
                enemyY[i] = 180;
            }

            for (int i=0; i<numOfEnemy; i++) {
                isEnemyAlive[i] = true;
                enemyMove[i] = 1;
            }

            for (int i=0; i<numOfEnemy; i++) {
                isEnemyMissileActive[i] = true;
                enemyMissileX[i] = enemyX[i] + enemyWidth/2;
                enemyMissileY[i] = enemyY[i];
                enemyMissileSpeed[i] = 1 + (i%6);
            }
        }

        /* 自機の描画 */
        public void drawMyPlane(Graphics g) {
            if (Math.abs(tempMyX - myX) < gap) {
                if (myX < 0) {
                    myX = 0;
                } else if (myX+myWidth > dimOfPanel.width) {
                    myX = dimOfPanel.width - myWidth;
                }
                tempMyX = myX;
                g.drawImage(imgMe, tempMyX, myY, this);
            } else {
                g.drawImage(imgMe, tempMyX, myY, this);
            }
        }

        /* 自機のミサイルの描画 */
        public void drawMyMissile(Graphics g) {
            if (isMyMissileActive) {
                // ミサイルの配置
                myMissileY -= 15;
                g.setColor(Color.white);
                g.fillRect(myMissileX, myMissileY, 6, 14);

                // 自機のミサイルの敵機各機への当たり判定
                for (int i=0; i<numOfEnemy; i++) {
                    if (isEnemyAlive[i]) {
                        if ((myMissileX >= enemyX[i]) &&
                            (myMissileX <= enemyX[i]+enemyWidth) &&
                            (myMissileY >= enemyY[i]) &&
                            (myMissileY <= enemyY[i]+enemyHeight)) {
                            isEnemyAlive[i] = false;
                            isMyMissileActive = false;
                            numOfAlive--;
                        }
                    }
                }

                // ミサイルがウィンドウ外に出たときのミサイルの再初期化
                if (myMissileY < 0) isMyMissileActive = false;
            }
        }

        /* 敵機の描画 */
        public void drawEnemyPlane(Graphics g) {
            for (int i=0; i<numOfEnemy; i++) {
                if (isEnemyAlive[i]) {
                    if (enemyX[i] > dimOfPanel.width - enemyWidth) {
                        enemyMove[i] = -1;
                    } else if (enemyX[i] < 0) {
                        enemyMove[i] = 1;
                    }
                    enemyX[i] += enemyMove[i]*2;
                    g.drawImage(imgEnemy, enemyX[i], enemyY[i], this);
                }
            }
        }

        /* 敵機のミサイルの描画 */
        public void drawEnemyMissile(Graphics g) {
            for (int i=0; i<numOfEnemy; i++) {
                // ミサイルの配置
                if (isEnemyMissileActive[i]) {
                    enemyMissileY[i] += enemyMissileSpeed[i] + (int)(Math.random() * 3);
                    g.setColor(new Color(
		    (int)(Math.random() * 256),
		    (int)(Math.random() * 256),
		    (int)(Math.random() * 256)
		    ));
                    g.fillRect(enemyMissileX[i], enemyMissileY[i], 5, 9);
                }

                // 敵機のミサイルの自機への当たり判定
                if ((enemyMissileX[i] >= tempMyX) &&
                    (enemyMissileX[i] <= tempMyX+myWidth) &&
                    (enemyMissileY[i]+4 >= myY) &&
                    (enemyMissileY[i]+4 <= myY+myHeight)) {
		    timer.stop();
                    isGameSet = true;
		    result = 2;
                }

                // ミサイルがウィンドウ外に出たとき、もしくはバリアにあたったときミサイルの再初期化
                if (enemyMissileY[i] > dimOfPanel.height ||
		   (isRightClick) &&
		   (enemyMissileX[i] >= barrierX) &&
		   (enemyMissileX[i] <= barrierX+barrierWidth) &&
		   (enemyMissileY[i]+5 >= barrierY) &&
		   (enemyMissileY[i]+5 <= barrierY+barrierHeight)) {
                    if (isEnemyAlive[i]) {
                        enemyMissileX[i] = enemyX[i] + enemyWidth/2;
                        enemyMissileY[i] = enemyY[i] + enemyHeight;
                    } else {
                        isEnemyMissileActive[i] = false;
                    }
                }
            }
        }
    }
}