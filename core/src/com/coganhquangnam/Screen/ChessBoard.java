package com.coganhquangnam.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.coganhquangnam.Actors.ChessPiece;
import com.coganhquangnam.Actors.OpeningSign;
import com.coganhquangnam.Actors.PlayBackButton;
import com.coganhquangnam.Engine.AI;



import java.util.ArrayList;

/**
 * Created by NGUYENGON on 2015/09/12.
 */
public class ChessBoard extends Stage {

    //Ve ban co
    public ShapeRenderer shapeRenderer;
    public static final int gocX = 40, gocY = 100, menuAreaHeight = 150, thickness =3;
    public static final int distance = 400;
    public static final int squaredistance = distance / 4;
    public static int pieceSize = 40;

    //actors
    public static ArrayList<ChessPiece> pieceCollection;
    public final OpeningSign HO, AO;
    private Skin skin;
    private Texture overlayBgTex;  // for WinOverlay

    //Constructor
    public ChessBoard(){
        super(new FitViewport(distance + 2*gocX, distance + gocY  + menuAreaHeight));  // chua biet truoc duoc
        //super(new ExtendViewport(distance, distance));

        //ChessBoard.distance = distance;

        shapeRenderer = new ShapeRenderer();
        pieceCollection = new ArrayList<ChessPiece>();
        initializePieces();
        // Opening Symbol
        AO = new OpeningSign(OpeningSign.COMPUTER_SIGN, this);
        HO = new OpeningSign(OpeningSign.HUMAN_SIGN, this);

        // Nut Undo
        PlayBackButton playBackButton = new PlayBackButton(this);
        this.addActor(playBackButton);

        // ─── Difficulty Dropdown Panel (vertical context-menu style) ───
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        final BitmapFont menuFont = skin.getFont("default-font");
        final GlyphLayout glyphLayout = new GlyphLayout();

        // 0=Easy, 1=Medium, 2=Hard  (Medium is default)
        final int[]    selectedDiff = {1};
        final String[] diffNames    = {"EASY", "MEDIUM", "HARD"};
        final int[]    diffDepths   = {2, 3, 4};
        AI.globalDepth = 3;

        final float ITEM_H  = 52f;
        final float SEP_H   = 2f;
        final float PANEL_W = 170f;
        final float PANEL_H = 3 * ITEM_H + 2 * SEP_H;
        final float PANEL_X = 10f;
        // Panel drops DOWN from the bottom of the hamburger button (y=570)
        final float PANEL_Y = 570f - PANEL_H;

        // ── Shared 1×1 textures ──
        Pixmap pm;

        pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(0.13f, 0.14f, 0.20f, 0.97f);   // dark navy panel bg
        pm.fill();
        final Texture panelBgTex = new Texture(pm);
        pm.dispose();

        pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(0.38f, 0.39f, 0.46f, 1f);       // grey separator
        pm.fill();
        final Texture sepTex = new Texture(pm);
        pm.dispose();

        pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(0.18f, 0.40f, 0.76f, 0.92f);    // blue highlight
        pm.fill();
        final Texture hlTex = new Texture(pm);
        pm.dispose();

        // ── The dropdown panel Actor ──
        final Actor diffPanel = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                float px = getX(), py = getY(), pw = getWidth(), ph = getHeight();

                // Panel background
                batch.setColor(1f, 1f, 1f, parentAlpha);
                batch.draw(panelBgTex, px, py, pw, ph);

                for (int i = 0; i < 3; i++) {
                    // itemY = bottom of this row (rows drawn top→bottom)
                    float itemY = py + ph - (i + 1) * ITEM_H - i * SEP_H;

                    // Separator line ABOVE this row (between rows)
                    if (i > 0) {
                        batch.setColor(0.38f, 0.39f, 0.46f, parentAlpha);
                        batch.draw(sepTex, px, itemY + ITEM_H, pw, SEP_H);
                    }

                    // Blue highlight for selected row
                    if (i == selectedDiff[0]) {
                        batch.setColor(0.18f, 0.40f, 0.76f, 0.92f * parentAlpha);
                        batch.draw(hlTex, px, itemY, pw, ITEM_H);
                    }

                    // Row label
                    menuFont.setColor(i == selectedDiff[0]
                            ? new Color(1f, 0.92f, 0.3f, 1f)   // gold for selected
                            : new Color(0.82f, 0.84f, 0.88f, 1f)); // light grey
                    glyphLayout.setText(menuFont, diffNames[i]);
                    menuFont.draw(batch, diffNames[i],
                            px + 18,
                            itemY + (ITEM_H + glyphLayout.height) / 2f);

                    batch.setColor(Color.WHITE);
                }
                batch.setColor(Color.WHITE);
            }
        };
        diffPanel.setBounds(PANEL_X, PANEL_Y, PANEL_W, PANEL_H);
        diffPanel.setVisible(false);

        diffPanel.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int btn) {
                // y=0 → bottom of panel, y=PANEL_H → top. Rows go top→bottom.
                float fromTop = diffPanel.getHeight() - y;
                int idx = Math.max(0, Math.min(2, (int)(fromTop / ITEM_H)));
                selectedDiff[0] = idx;
                AI.globalDepth  = diffDepths[idx];
                diffPanel.setVisible(false);   // close after selection
                return true;
            }
        });

        // ─── Labels (added FIRST so diffPanel renders on top of them) ───
        // "Difficulty" label below hamburger (centerX=70, hamburger bottom=570)
        this.addActor(new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                glyphLayout.setText(menuFont, "Difficulty");
                menuFont.setColor(0.78f, 0.80f, 0.88f, parentAlpha);
                menuFont.draw(batch, "Difficulty",
                        70f - glyphLayout.width / 2f,
                        565f);
                batch.setColor(Color.WHITE);
            }
        });

        // "Back" label below PlayBackButton (centerX=240, bottom=565)
        this.addActor(new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                glyphLayout.setText(menuFont, "Back");
                menuFont.setColor(0.78f, 0.80f, 0.88f, parentAlpha);
                menuFont.draw(batch, "Back",
                        240f - glyphLayout.width / 2f,
                        560f);
                batch.setColor(Color.WHITE);
            }
        });

        // "Guide" label below GuideButton (centerX=410, bottom=560)
        this.addActor(new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                glyphLayout.setText(menuFont, "Guide");
                menuFont.setColor(0.78f, 0.80f, 0.88f, parentAlpha);
                menuFont.draw(batch, "Guide",
                        410f - glyphLayout.width / 2f,
                        560f);
                batch.setColor(Color.WHITE);
            }
        });

        // diffPanel added AFTER labels → draws on top (covers "Difficulty" when open)
        this.addActor(diffPanel);

        // ─── Hamburger Button (≡) ───
        final Actor hamburgerBtn = new Actor() {
            private Texture lineTex;
            {
                Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                p.setColor(Color.WHITE);
                p.fill();
                lineTex = new Texture(p);
                p.dispose();
            }
            @Override
            public void draw(Batch batch, float parentAlpha) {
                float bx = getX(), by = getY(), bw = getWidth(), bh = getHeight();
                float lineW  = bw * 0.60f;
                float lineH  = 5f;
                float gap    = 8f;
                float sx     = bx + (bw - lineW) / 2f;
                float totalH = 3 * lineH + 2 * gap;
                float sy     = by + (bh - totalH) / 2f;

                batch.setColor(0.18f, 0.18f, 0.24f, 0.80f * parentAlpha);
                batch.draw(lineTex, bx, by, bw, bh);

                batch.setColor(1f, 1f, 1f, parentAlpha);
                batch.draw(lineTex, sx, sy,                  lineW, lineH);
                batch.draw(lineTex, sx, sy + lineH + gap,    lineW, lineH);
                batch.draw(lineTex, sx, sy + 2*(lineH + gap), lineW, lineH);
                batch.setColor(Color.WHITE);
            }
        };
        hamburgerBtn.setBounds(45, 570, 50, 80);

        hamburgerBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int btn) {
                diffPanel.setVisible(!diffPanel.isVisible());
                return true;
            }
        });

        this.addActor(hamburgerBtn);

        // ─── Guide Button (i / ?) ───
        final Actor guideBtn = new Actor() {
            private Texture circleTex;
            {
                Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                p.setColor(Color.WHITE);
                p.fill();
                circleTex = new Texture(p);
                p.dispose();
            }
            @Override
            public void draw(Batch batch, float parentAlpha) {
                float bx = getX(), by = getY(), bw = getWidth(), bh = getHeight();
                
                batch.setColor(0.18f, 0.18f, 0.24f, 0.80f * parentAlpha);
                batch.draw(circleTex, bx, by, bw, bh);

                menuFont.getData().setScale(1.6f);
                menuFont.setColor(Color.WHITE);
                glyphLayout.setText(menuFont, "?");
                menuFont.draw(batch, "?",
                        bx + (bw - glyphLayout.width) / 2f,
                        by + (bh + glyphLayout.height) / 2f);
                menuFont.getData().setScale(1f);
                batch.setColor(Color.WHITE);
            }
        };
        guideBtn.setBounds(385, 570, 50, 80);

        // ─── WIN / LOSE Overlay ───
        Pixmap ovPm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        ovPm.setColor(0f, 0f, 0f, 0.72f);
        ovPm.fill();
        overlayBgTex = new Texture(ovPm);
        ovPm.dispose();

        final float WW = distance + 2 * gocX;   // world width  = 480
        final float WH = distance + gocY + menuAreaHeight; // world height = 650

        final int[] guidePage = {0}; // 0, 1, 2
        final String[][] guideTexts = {
            {
                "             HOW TO PLAY CO GANH",
                "",
                "        PAGE 1/3: BOARD & MOVEMENT",
                "",
                "- The board consists of 25 intersections.",
                "- Player A (You): Orange Bitcoin",
                "- Player B (Computer): Blue Ethereum",
                "- At start, 8 pieces of each player are",
                "  placed along the outer edge of the board.",
                "",
                "- Movement: Take turns moving one piece",
                "  to an adjacent empty intersection along",
                "  horizontal, vertical, or diagonal lines."
            },
            {
                "             HOW TO PLAY CO GANH",
                "",
                "         PAGE 2/3: GANH & VAY RULES",
                "",
                "- Ganh (Capture): Move your piece directly",
                "  between two opponent pieces (forming a line).",
                "  Those two opponent pieces will be captured",
                "  and change to your coin type.",
                "  *Note: Only happens on your active move.",
                "   If opponent moves between you, they are safe.",
                "  *Ganh Chau: Capture up to 4 or 6 pieces.",
                "",
                "- Vay (Cornered): Completely surround opponent",
                "  pieces so they have no legal moves left.",
                "  The trapped pieces change to your coin type."
            },
            {
                "             HOW TO PLAY CO GANH",
                "",
                "        PAGE 3/3: OPEN TRAP & WINNING",
                "",
                "- Open Trap (Mo / Chap):",
                "  A high-level strategy where you move your",
                "  piece to leave an empty spot between two",
                "  of your own pieces, baiting the opponent.",
                "",
                "- FORCED CAPTURE (CRITICAL RULE):",
                "  When a player declares 'Open' (Mo), the",
                "  opponent MUST move their piece into that",
                "  empty spot to capture on their next turn",
                "  (if they have a legal move to that spot).",
                "",
                "- Winning: Capture all 16 pieces on the board,",
                "  or block opponent from making any legal moves."
            }
        };

        final Actor guideOverlay = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.setColor(0f, 0f, 0f, 0.85f * parentAlpha);
                batch.draw(overlayBgTex, 0, 0, WW, WH);

                float cardW = 440f, cardH = 500f;
                float cardX = (WW - cardW) / 2f;
                float cardY = (WH - cardH) / 2f;
                batch.setColor(0.08f, 0.09f, 0.15f, 0.98f * parentAlpha);
                batch.draw(overlayBgTex, cardX, cardY, cardW, cardH);

                batch.setColor(0.18f, 0.40f, 0.76f, parentAlpha);
                batch.draw(overlayBgTex, cardX, cardY + cardH - 6f, cardW, 6f);

                menuFont.getData().setScale(1.15f);
                menuFont.setColor(new Color(0.9f, 0.92f, 0.95f, 1f));
                
                String[] lines = guideTexts[guidePage[0]];
                float startY = cardY + cardH - 30f;
                for (int i = 0; i < lines.length; i++) {
                    menuFont.draw(batch, lines[i], cardX + 22, startY - i * 26);
                }

                menuFont.getData().setScale(1.25f);
                
                if (guidePage[0] > 0) {
                    menuFont.setColor(0.2f, 0.6f, 1f, parentAlpha);
                    glyphLayout.setText(menuFont, "<- PREV");
                    menuFont.draw(batch, "<- PREV", cardX + 30, cardY + 35);
                }
                
                if (guidePage[0] < 2) {
                    menuFont.setColor(0.2f, 0.6f, 1f, parentAlpha);
                    glyphLayout.setText(menuFont, "NEXT ->");
                    menuFont.draw(batch, "NEXT ->", cardX + cardW - 30 - glyphLayout.width, cardY + 35);
                }

                menuFont.setColor(1f, 0.3f, 0.3f, parentAlpha);
                glyphLayout.setText(menuFont, "CLOSE");
                menuFont.draw(batch, "CLOSE", WW / 2f - glyphLayout.width / 2f, cardY + 35);

                menuFont.getData().setScale(1f);
                batch.setColor(Color.WHITE);
            }
        };
        guideOverlay.setBounds(0, 0, WW, WH);
        guideOverlay.setVisible(false);

        guideOverlay.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int btn) {
                if (y >= 75 && y <= 125) {
                    if (x >= 20 && x <= 140 && guidePage[0] > 0) {
                        guidePage[0]--;
                        return true;
                    }
                    if (x >= 340 && x <= 460 && guidePage[0] < 2) {
                        guidePage[0]++;
                        return true;
                    }
                    if (x >= 180 && x <= 300) {
                        guideOverlay.setVisible(false);
                        return true;
                    }
                }
                return true;
            }
        });

        guideBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int btn) {
                guidePage[0] = 0;
                guideOverlay.setVisible(true);
                return true;
            }
        });

        this.addActor(guideBtn);
        this.addActor(guideOverlay);

        // Shared message state — read by winOverlay.draw(), written by AI callback
        final String[] overlayMsg = {"", ""};  // [0]=title, [1]=subtitle

        final Actor winOverlay = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                String line1 = overlayMsg[0];
                String line2 = overlayMsg[1];

                // Full-screen dim
                batch.setColor(0f, 0f, 0f, 0.72f * parentAlpha);
                batch.draw(overlayBgTex, 0, 0, WW, WH);

                // Card background
                float cardW = 340f, cardH = 160f;
                float cardX = (WW - cardW) / 2f;
                float cardY = (WH - cardH) / 2f;
                batch.setColor(0.10f, 0.12f, 0.20f, 0.97f * parentAlpha);
                batch.draw(overlayBgTex, cardX, cardY, cardW, cardH);

                // Accent top bar (green=win, red=lose)
                boolean isWin = line1.contains("WIN");
                batch.setColor(isWin ? 0.18f : 0.80f,
                               isWin ? 0.75f : 0.18f,
                               isWin ? 0.35f : 0.18f,
                               parentAlpha);
                batch.draw(overlayBgTex, cardX, cardY + cardH - 6f, cardW, 6f);

                // Title (big)
                menuFont.getData().setScale(3.0f);
                menuFont.setColor(isWin
                        ? new Color(0.25f, 1.0f, 0.45f, 1f)
                        : new Color(1.0f, 0.30f, 0.30f, 1f));
                glyphLayout.setText(menuFont, line1);
                menuFont.draw(batch, line1,
                        WW / 2f - glyphLayout.width / 2f,
                        cardY + cardH - 22f);

                // Subtitle
                menuFont.getData().setScale(1.4f);
                menuFont.setColor(0.82f, 0.84f, 0.90f, 1f);
                glyphLayout.setText(menuFont, line2);
                menuFont.draw(batch, line2,
                        WW / 2f - glyphLayout.width / 2f,
                        cardY + 48f);

                menuFont.getData().setScale(1f);
                batch.setColor(Color.WHITE);
            }
        };
        winOverlay.setBounds(0, 0, WW, WH);
        winOverlay.setVisible(false);
        this.addActor(winOverlay);

        // Register callback — AI calls this instead of JOptionPane
        AI.onGameEnd = new AI.GameEndListener() {
            private void show(String title, String subtitle) {
                overlayMsg[0] = title;
                overlayMsg[1] = subtitle;
                winOverlay.setVisible(true);
                winOverlay.clearActions();
                winOverlay.setScale(0.3f);
                winOverlay.setOrigin(WW / 2f, WH / 2f);
                winOverlay.addAction(Actions.scaleTo(1f, 1f, 0.35f,
                        com.badlogic.gdx.math.Interpolation.swingOut));
            }
            @Override public void onHumanWins()   { show("YOU WIN!",  "Congratulations!"); }
            @Override public void onComputerWins() { show("YOU LOSE", "Better luck next time!"); }
        };

/*        for(ChessPiece cp : pieceCollection)
        {
            System.out.println(cp.getBoardCoord().x + "___" + cp.getBoardCoord().y);
        }*/
    }


    @Override
    public void draw() {
        getBatch().setProjectionMatrix(getCamera().combined);
        getBatch().begin();
        getBatch().draw(com.coganhquangnam.gesture.Assets.screenBackground, 0, 0, getViewport().getWorldWidth(), getViewport().getWorldHeight());
        getBatch().draw(com.coganhquangnam.gesture.Assets.boardBackground, gocX, gocY, distance, distance);
        getBatch().end();

        drawBoard();
        super.draw();
    }

    public void drawBoard()
    {


        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.88f, 0.75f, 0.52f, 1.0f)); // Warm golden cream
        for(int row=0; row<5; row++)
        {
            shapeRenderer.rectLine(gocX, row * squaredistance + gocY, distance + gocX, row * squaredistance + gocY, thickness);
        }
        for(int column=0; column<5; column++)
        {
            shapeRenderer.rectLine(column * squaredistance + gocX, gocY, column * squaredistance + gocX, distance + gocY, thickness);
        }
        shapeRenderer.rectLine(2 * squaredistance + gocX, gocY, gocX, 2 * squaredistance + gocY, thickness);
        shapeRenderer.rectLine(distance + gocX, gocY, gocX, distance + gocY, thickness);
        shapeRenderer.rectLine(distance + gocX, 2 * squaredistance + gocY, 2 * squaredistance + gocX, distance + gocY, thickness);
        shapeRenderer.rectLine(gocX, gocY, distance + gocX, distance + gocY, thickness);
        shapeRenderer.rectLine(2 * squaredistance + gocX, gocY, distance + gocX, 2 * squaredistance + gocY, thickness);
        shapeRenderer.rectLine(gocX, 2 * squaredistance + gocY, 2 * squaredistance + gocX, distance + gocY, thickness);

        shapeRenderer.end();

    }

    public void repaint()
    {
        //Gdx.gl.glClearColor(0,0,0,1);
        for(ChessPiece p : pieceCollection)
        {
            p.resetFaction();
        }

    }
    public void initializePieces()
    {
        ChessPiece chessPiece = null;

        for(int i=0; i<5; i++)
            for(int j=0; j<5; j++)
            {
                //                        if (AI.chessBoard[i][j].equals("a")) {
//                            chessPiece = new ChessPiece(new Vector2(i, j), pieceSize, ChessKind.DARK, this);
//                        }
//                        if (AI.chessBoard[i][j].equals("A")) {
//                            chessPiece = new ChessPiece(new Vector2(i, j), pieceSize, ChessKind.LIGHT, this);
//                        }
                if(AI.chessBoard[i][j].equals("a") || AI.chessBoard[i][j].equals("A")) {
                    chessPiece = new ChessPiece(new Vector2(i, j), pieceSize, this);
                    pieceCollection.add(chessPiece);

                }
                if (chessPiece != null) addActor(chessPiece);

            }
        }

    public  void showNextPosition()
    {

        if(ChessPiece.listNextPosition != null) {
            for (int m = 0; m < ChessPiece.listNextPosition.size(); m++) {
                addActor(ChessPiece.listNextPosition.get(m));
            }
        }
    }
    public void removeOldNextPosition()
    {
        for (int m = 0; m < ChessPiece.listNextPosition.size(); m++) {
            ChessPiece.listNextPosition.get(m).remove();
        }
    }

    // Xoa het cac Listener
    public void DontTouchMe()
    {
        for(ChessPiece cp : pieceCollection)
        {
            for(EventListener e : cp.getListeners())
            {
                // Can than loi co the xay ra tai day !!!
                if(e != cp.debuggingShow )
                                cp.removeListener(e);
            }

        }
    }
    // Tao lai cac Listener cho cac ChessPieces
    public void touchMePlease()
    {
        for(int k=0; k<pieceCollection.size(); k++)
        {
            ChessPiece cp = pieceCollection.get(k);
            cp.addListener(cp.touchedYou);
            //cp.setTouchable(Touchable.enabled);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (skin != null) skin.dispose();
        if (overlayBgTex != null) overlayBgTex.dispose();
    }

}
