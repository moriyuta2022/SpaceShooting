import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SpaceShooting extends JFrame {
    final int windowWidth = 800;
    final int windowHeight = 800;
    public static boolean isBattle = false;	//�^���O���[�o���ϐ�

    public static void main(String[] args){
        new SpaceShooting();
    }

    /* �t���[���ݒ� */
    public SpaceShooting() {
        Dimension dimOfScreen = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(dimOfScreen.width/2 - windowWidth/2, dimOfScreen.height/2 - windowHeight/2, windowWidth, windowHeight);
        setResizable(false);
        setTitle("�X�y�[�X�V���[�e�B���O");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
	panelCange();
    }

    /* �p�l���؂�ւ� */
    public void panelCange() {
	// �^�C�g������퓬��ʂ�
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

    /* �^�C�g����� */
    public class TitleJPanel extends MyJPanel implements ActionListener {
	/* �^�C�g����ʂɊւ���ϐ� */
	JButton startButton, barrierButton;
	JLabel titleLabel, explanationLabel1, explanationLabel2, explanationLabel3;
	boolean barrierDetail = false;		// �o���A�ڍו\�����ǂ����𔻒f����

	/* �R���X�g���N�^�i�Q�[���J�n���̏������j*/
	public TitleJPanel() {
	    setLayout(null);

	    // ���x���̍쐬
	    titleLabel = new JLabel("�X�y�[�X�V���[�e�B���O");
	    explanationLabel1 = new JLabel("�� �� �� ��");
	    explanationLabel2 = new JLabel("<html>���N���b�N�F�~�T�C������<br/>�E�N���b�N�F�o���A<br/>E&thinsp;s&thinsp;c�L�[&emsp;�F�|�[�Y���j���[</html>");
	    explanationLabel3 = new JLabel("<html>&emsp;&emsp;&emsp;�o���A�œG�̖ҍU�����߂������I<br/>&emsp;&emsp;�ˌ��サ�΂炭�̓o���A������Ȃ���I<br/>�o���A���͓����Ȃ����A�e�����ĂȂ��̂Œ��ӁI</html>");

	    titleLabel.setForeground(Color.white);
	    explanationLabel1.setForeground(Color.white);
	    explanationLabel2.setForeground(Color.white);
	    explanationLabel3.setForeground(Color.white);

	    titleLabel.setHorizontalAlignment(titleLabel.CENTER);
	    explanationLabel1.setHorizontalAlignment(explanationLabel1.CENTER);
	    explanationLabel2.setHorizontalAlignment(explanationLabel2.CENTER);
	    explanationLabel3.setHorizontalAlignment(explanationLabel3.CENTER);

	    titleLabel.setFont(new Font("HG��������-PRO", Font.PLAIN, 62));
	    explanationLabel1.setFont(new Font("HG��������-PRO", Font.PLAIN, 35));
	    explanationLabel2.setFont(new Font("HG��������-PRO", Font.PLAIN, 30));
	    explanationLabel3.setFont(new Font("HG��������-PRO", Font.PLAIN, 30));

	    titleLabel.setBounds(0, 0 - 200, windowWidth, windowHeight);
	    explanationLabel1.setBounds(0, 0 - 100, windowWidth, windowHeight);
	    explanationLabel2.setBounds(0, 0, windowWidth, windowHeight);
	    explanationLabel3.setBounds(0, 0 + 120, windowWidth, windowHeight);

	    add(titleLabel);
	    add(explanationLabel1);
	    add(explanationLabel2);
	    add(explanationLabel3);

	    // �{�^���̍쐬
            startButton = new JButton("�Q�[���X�^�[�g");
	    startButton.addActionListener(this);
	    startButton.setForeground(Color.white);
            startButton.setBackground(new Color(70, 70, 150));
	    startButton.setHorizontalAlignment(startButton.CENTER);
	    startButton.setFont(new Font("HG��������-PRO", Font.PLAIN, 50));
	    startButton.setBounds(100, windowHeight - 150, windowWidth - 200, 70);
	    add(startButton);
	}

	/* �p�l����̕`�� */
	public void paintComponent(Graphics g) {
	    g.setColor(new Color(60, 60, 60));
	    g.fillRect(0, 0 + 120, windowWidth, windowHeight - 300);
	}

	/* ��莞�Ԃ��Ƃ̏����iActionListener �ɑ΂��鏈���j*/
        public void actionPerformed(ActionEvent e) {
	    //�X�^�[�g�{�^���������ꂽ��p�l���`�F���W���\�b�h��
	    if (e.getSource() == startButton) {
		SpaceShooting.isBattle = true;
		panelCange();
	        repaint();
	    }
        }
    }

    /* �퓬��� */
    public class MyJPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {

        /* �S�̂̐ݒ�Ɋւ���ϐ� */
        Dimension dimOfPanel;
        Timer timer;
        ImageIcon iconMe, iconEnemy;
        Image imgMe, imgEnemy;
        int FrameRate = 1000/120;
	int Pause = 0;

	/* �Q�[���I���Ɋւ���ϐ�*/
        JButton exit;
	boolean isGameSet = false;
	int result = 0;
	JLabel winLabel, loseLabel, pTextLabel1, pTextLabel2;

        /* ���@�Ɋւ���ϐ� */
        int myWidth, myHeight;
        int myX, myY, tempMyX;
        int gap = 100;
        int myMissileX, myMissileY;
	int barrierWidth = 70, barrierHeight = 6;
	int barrierX, barrierY;
        boolean isMyMissileActive;
	boolean isRightClick = false;

        /* �G�@�Ɋւ���ϐ� */
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

        /* �R���X�g���N�^�i�Q�[���J�n���̏������j*/
        public MyJPanel() {
            // �S�̂̐ݒ�
            setBackground(Color.black);
            addMouseListener(this);
            addMouseMotionListener(this);
	    addKeyListener(this);
            timer = new Timer(FrameRate, this);
            timer.start();

            // �摜�̎�荞��
            imgMe = getImg("jiki.jpg");
            myWidth = imgMe.getWidth(this);
            myHeight = imgMe.getHeight(this);

            imgEnemy = getImg("teki.jpg");
            enemyWidth = imgEnemy.getWidth(this);
            enemyHeight = imgEnemy.getHeight(this);

            // ���@�ƓG�@�̏�����
            initMyPlane();
            initEnemyPlane();
        }

        /* �p�l����̕`�� */
        public void paintComponent(Graphics g) {
            dimOfPanel = getSize();
            super.paintComponent(g);

            // �e�v�f�̕`��
            drawMyPlane(g);       // ���@
            drawMyMissile(g);     // ���@�̃~�T�C��
            drawEnemyPlane(g);    // �G�@
            drawEnemyMissile(g);  // �G�@�̃~�T�C��
	    rightClick(g);	  // �o���A

	    // ���x���̍쐬
	    winLabel = new JLabel("�Q�[���N���A�I");
	    loseLabel = new JLabel("�Q�[���I�[�o�[");
	    pTextLabel1 = new JLabel("�`�|�[�Y���`");
	    pTextLabel2 = new JLabel("Esc�L�[�������ƍĊJ");
	    winLabel.setForeground(Color.white);
	    loseLabel.setForeground(Color.white);
	    pTextLabel1.setForeground(Color.white);
	    pTextLabel2.setForeground(Color.white);
	    winLabel.setHorizontalAlignment(winLabel.CENTER);
	    loseLabel.setHorizontalAlignment(loseLabel.CENTER);
	    pTextLabel1.setHorizontalAlignment(pTextLabel1.CENTER);
	    pTextLabel2.setHorizontalAlignment(pTextLabel2.CENTER);
	    winLabel.setFont(new Font("HG��������-PRO", Font.PLAIN, 80));
	    loseLabel.setFont(new Font("HG��������-PRO", Font.PLAIN, 70));
	    pTextLabel1.setFont(new Font("HG��������-PRO", Font.PLAIN, 80));
	    pTextLabel2.setFont(new Font("HG��������-PRO", Font.PLAIN, 38));
	    winLabel.setBounds(0, 0 - 50, windowWidth, windowHeight);
	    loseLabel.setBounds(0, 0 - 50, windowWidth, windowHeight);
	    pTextLabel1.setBounds(0, 0 - 50, windowWidth, windowHeight);
	    pTextLabel2.setBounds(0, 0 + 50, windowWidth, windowHeight);

	    // �{�^���̍쐬
	    exit = new JButton("�I��");
	    exit.addActionListener(this);
	    exit.setFont(new Font("HG��������-PRO", Font.PLAIN, 20));
	    exit.setBackground(Color.white);
	    exit.setBounds(windowWidth - 100, windowHeight - 100, 80, 30);

            // �G�@��S�@���Ă������̏I������
            if (numOfAlive == 0) {
		timer.stop();
                isGameSet = true;
		result = 1;
            }

	    // �Q�[���I���{�^���̕`��
	    if (isGameSet) {
	        setLayout(null);
                add(exit);
	    }

	    // �Q�[���I�����x���̕`��
	    if (result == 1) {
		add(winLabel);
	    } else if (result == 2) {
		add(loseLabel);
	    }

	    // �|�[�Y��ʐ؂�ւ��̕`��
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

	/* KeyListener �ɑ΂��鏈�� */
	// �L�[�{�^������������
	public void keyPressed(KeyEvent e) {
	    // Esc �L�[�������ꂽ���ʃX�g�b�v�A������x�����Ƃ��Ƃɖ߂�
	    if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !isGameSet && isBattle && Pause == 0) {
		Pause = 1;
	    } else if(e.getKeyCode() == KeyEvent.VK_ESCAPE && Pause == 2) {
		Pause = 3;
		repaint();
	    }
	}

	// �L�[�{�^���𗣂�
	public void keyReleased(KeyEvent e) {
	}

	// �L�[�{�^������͂���
	public void keyTyped(KeyEvent e) {
	}

        /* ��莞�Ԃ��Ƃ̏����iActionListener �ɑ΂��鏈���j*/
	// �I���ݒ�
        public void actionPerformed(ActionEvent e) {
	    if (e.getSource() == exit) {
		System.exit(0);
	    }
            repaint();
        }

        /* MouseListener �ɑ΂��鏈�� */
        // �}�E�X�{�^�����N���b�N����
        public void mouseClicked(MouseEvent e) {
        }

        // �}�E�X�{�^������������
        public void mousePressed(MouseEvent e) {
	    // ���N���b�N�E�E�N���b�N����
	    if (e.getButton() == MouseEvent.BUTTON1){
		// ���N���b�N�Ȃ�ˌ�
		if (!isMyMissileActive && !isRightClick) {
		    myMissileX = tempMyX + myWidth/2;
                    myMissileY = myY;
                    isMyMissileActive = true;
		}
	    // �E�N���b�N�Ȃ�o���A
	    }else if (e.getButton() == MouseEvent.BUTTON3){
		if (!isMyMissileActive) {
		    isRightClick = true;
		}
	    }
        }

	//�E�N���b�N�������ꂽ��o���A��`�悷��
	public void rightClick(Graphics g) {
	    barrierX = tempMyX - 20;
	    barrierY = myY - 20;
	    if (isRightClick) {
		g.setColor(Color.blue);
		g.fillRect(barrierX, barrierY, barrierWidth, barrierHeight);
	    }
	}

        // �}�E�X�{�^���𗣂�
        public void mouseReleased(MouseEvent e) {
	    if (e.getButton() == MouseEvent.BUTTON3){
		isRightClick = false;
	    }
        }

        // �}�E�X���̈�O�֏o��
        public void mouseExited(MouseEvent e) {
        }

        // �}�E�X���̈���ɓ���
        public void mouseEntered(MouseEvent e) {
        }

        /* MouseMotionListener �ɑ΂��鏈�� */
        // �}�E�X�𓮂���
        public void mouseMoved(MouseEvent e) {
	    if (!isRightClick) {
                myX = e.getX();
	    }
        }

        // �}�E�X���h���b�O����
        public void mouseDragged(MouseEvent e) {
	    if (!isRightClick) {
                myX = e.getX();
	    }
        }

        /* �摜�t�@�C������ Image �N���X�ւ̕ϊ� */
        public Image getImg(String filename) {
            ImageIcon icon = new ImageIcon(filename);
            Image img = icon.getImage();

            return img;
        }

        /* ���@�̏����� */
        public void initMyPlane() {
            myX = windowWidth / 2;
            myY = windowHeight - 100;
            tempMyX = windowWidth / 2;
            isMyMissileActive = false;
        }

        /* �G�@�̏����� */
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

        /* ���@�̕`�� */
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

        /* ���@�̃~�T�C���̕`�� */
        public void drawMyMissile(Graphics g) {
            if (isMyMissileActive) {
                // �~�T�C���̔z�u
                myMissileY -= 15;
                g.setColor(Color.white);
                g.fillRect(myMissileX, myMissileY, 6, 14);

                // ���@�̃~�T�C���̓G�@�e�@�ւ̓����蔻��
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

                // �~�T�C�����E�B���h�E�O�ɏo���Ƃ��̃~�T�C���̍ď�����
                if (myMissileY < 0) isMyMissileActive = false;
            }
        }

        /* �G�@�̕`�� */
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

        /* �G�@�̃~�T�C���̕`�� */
        public void drawEnemyMissile(Graphics g) {
            for (int i=0; i<numOfEnemy; i++) {
                // �~�T�C���̔z�u
                if (isEnemyMissileActive[i]) {
                    enemyMissileY[i] += enemyMissileSpeed[i] + (int)(Math.random() * 3);
                    g.setColor(new Color(
		    (int)(Math.random() * 256),
		    (int)(Math.random() * 256),
		    (int)(Math.random() * 256)
		    ));
                    g.fillRect(enemyMissileX[i], enemyMissileY[i], 5, 9);
                }

                // �G�@�̃~�T�C���̎��@�ւ̓����蔻��
                if ((enemyMissileX[i] >= tempMyX) &&
                    (enemyMissileX[i] <= tempMyX+myWidth) &&
                    (enemyMissileY[i]+4 >= myY) &&
                    (enemyMissileY[i]+4 <= myY+myHeight)) {
		    timer.stop();
                    isGameSet = true;
		    result = 2;
                }

                // �~�T�C�����E�B���h�E�O�ɏo���Ƃ��A�������̓o���A�ɂ��������Ƃ��~�T�C���̍ď�����
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