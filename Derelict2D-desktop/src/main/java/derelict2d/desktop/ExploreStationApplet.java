package derelict2d.desktop;

import br.odb.derelict.core.DerelictGame;
import br.odb.derelict.graphics2d.DerelictGraphicsAdapter;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.gamelib.swing.SwingMediaPlayer;
import br.odb.gamelib.swing.SwingTextClientAdapter;
import br.odb.gamerendering.rendering.AssetManager;
import br.odb.gamerendering.rendering.DisplayList;
import br.odb.gameworld.Location;
import br.odb.gameworld.exceptions.InvalidLocationException;
import br.odb.gameworld.exceptions.InvalidSlotException;
import br.odb.libsvg.SVGParsingUtils;
import br.odb.utils.Direction;
import br.odb.utils.FileServerDelegate;
import br.odb.utils.math.Vec2;
import static java.applet.Applet.newAudioClip;
import java.applet.AudioClip;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author monty
 */
public class ExploreStationApplet extends javax.swing.JApplet implements FileServerDelegate {

    DerelictGraphicsAdapter adapter = new DerelictGraphicsAdapter();
    private DerelictGame game;
    private AssetManager resManager;
    private DisplayList node;
    AudioClip playerSound;
    AudioClip ding;
    AudioClip fiveStep;

    /**
     * Initializes the applet ExploreStationApplet
     */
    @Override
    public void init() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ExploreStationApplet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ExploreStationApplet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ExploreStationApplet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ExploreStationApplet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the applet */
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    initComponents();
                }
            });
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }

        game = (DerelictGame) new DerelictGame().setAppName("DERELICT1D").setAuthorName("Daniel Monteiro")
                .setLicenseName("3-Clause BSD").setReleaseYear(2014);

        resManager = new AssetManager();

        game.setApplicationClient(new SwingTextClientAdapter(txtOutput, resManager));

        try {

            resManager.putGraphic("floor1", SVGParsingUtils
                    .readSVG(openAsset("overview-map/floor1.svg")));
            resManager.putGraphic("floor2", SVGParsingUtils
                    .readSVG(openAsset("overview-map/floor2.svg")));
            resManager.putGraphic("floor3", SVGParsingUtils
                    .readSVG(openAsset("overview-map/floor3.svg")));

            resManager.putGraphic("backdrop", SVGParsingUtils
                    .readSVG(openAsset("overview-map/backdrop.svg")));

            resManager.putGraphic("heroGraphic", SVGParsingUtils
                    .readSVG(openAsset("overview-map/astronaut-icon.svg")));
            resManager.putGraphic("blowtorch",
                    SVGParsingUtils.readSVG(openAsset("items/blowtorch.svg")));
            resManager.putGraphic("bomb-remote-controller", SVGParsingUtils
                    .readSVG(openAsset("items/bomb-remote-controller.svg")));
            resManager.putGraphic("book",
                    SVGParsingUtils.readSVG(openAsset("items/book.svg")));
            resManager.putGraphic("comm-system", SVGParsingUtils
                    .readSVG(openAsset("items/comm-system.svg")));
            resManager.putGraphic("atmosphere-purifier", SVGParsingUtils
                    .readSVG(openAsset("items/atmosphere-purifier.svg")));
            resManager.putGraphic("keycard-for-high-rank", SVGParsingUtils
                    .readSVG(openAsset("items/keycard-for-high-rank.svg")));
            resManager.putGraphic("keycard-for-root-access", SVGParsingUtils
                    .readSVG(openAsset("items/keycard-for-lab-access.svg")));
            resManager.putGraphic("keycard-for-low-rank", SVGParsingUtils
                    .readSVG(openAsset("items/keycard-for-low-rank.svg")));
            resManager.putGraphic("lab-equipment", SVGParsingUtils
                    .readSVG(openAsset("items/lab-equipment.svg")));

            resManager.putGraphic("electric-experiment", SVGParsingUtils
                    .readSVG(openAsset("items/lab-equipment.svg")));

            resManager.putGraphic("magboots",
                    SVGParsingUtils.readSVG(openAsset("items/magboots.svg")));
            resManager.putGraphic("metal-plate", SVGParsingUtils
                    .readSVG(openAsset("items/metal-plate.svg")));
			resManager
			.putGraphic("metal-scrap", SVGParsingUtils
					.readSVG(openAsset("items/metal-plate.svg")));
			resManager
			.putGraphic("metal-sheet", SVGParsingUtils
					.readSVG(openAsset("items/metal-plate.svg")));            
            
            resManager.putGraphic("plasma-gun",
                    SVGParsingUtils.readSVG(openAsset("items/plasma-gun.svg")));
            resManager.putGraphic("plasma-pellet", SVGParsingUtils
                    .readSVG(openAsset("items/plasma-pellet.svg")));
            resManager.putGraphic("plastic-pipes", SVGParsingUtils
                    .readSVG(openAsset("items/plastic-pipes.svg")));
            resManager.putGraphic("time-bomb",
                    SVGParsingUtils.readSVG(openAsset("items/time-bomb.svg")));
            resManager.putGraphic("computer-stand", SVGParsingUtils
                    .readSVG(openAsset("items/computer-stand.svg")));
            resManager.putGraphic("ship-ignition-key", SVGParsingUtils
                    .readSVG(openAsset("items/ship-ignition-key.svg")));

            playerSound = newAudioClip(getClass().getResource("/playersounds.wav"));
            fiveStep = newAudioClip(getClass().getResource("/fivesteps.wav"));
            ding = newAudioClip(getClass().getResource("/ding.wav"));

            resManager.registerMediaPlayer("pick", new SwingMediaPlayer(newAudioClip(getClass().getResource("/pick.wav"))));
            resManager.registerMediaPlayer("drop", new SwingMediaPlayer(newAudioClip(getClass().getResource("/drop.wav"))));
            resManager.registerMediaPlayer("bonk", new SwingMediaPlayer(newAudioClip(getClass().getResource("/bonk.wav"))));
            resManager.registerMediaPlayer("spooky1", new SwingMediaPlayer(newAudioClip(getClass().getResource("/spooky1.wav"))));
            resManager.registerMediaPlayer("spooky2", new SwingMediaPlayer(newAudioClip(getClass().getResource("/spooky2.wav"))));
            resManager.registerMediaPlayer("spooky3", new SwingMediaPlayer(newAudioClip(getClass().getResource("/spooky3.wav"))));
            resManager.registerMediaPlayer("click", new SwingMediaPlayer(newAudioClip(getClass().getResource("/click.wav"))));
            resManager.registerMediaPlayer("blowtorch-turned-on", new SwingMediaPlayer(newAudioClip(getClass().getResource("/blowtorchon.wav"))));
//            resManager.registerMediaPlayer( "blowtorch-turned-off", new SwingMediaPlayer( newAudioClip(getClass().getResource("/blowtorchoff.wav") )  ) );
            resManager.registerMediaPlayer("blowtorch-used", new SwingMediaPlayer(newAudioClip(getClass().getResource("/blowtorchuse.wav"))));
            resManager.registerMediaPlayer("shot", new SwingMediaPlayer(newAudioClip(getClass().getResource("/shot.wav"))));
            resManager.registerMediaPlayer("magbootson", new SwingMediaPlayer(newAudioClip(getClass().getResource("/magbootson.wav"))));
            resManager.registerMediaPlayer("magbootsoff", new SwingMediaPlayer(newAudioClip(getClass().getResource("/magbootsoff.wav"))));
            resManager.registerMediaPlayer("magbootsused", new SwingMediaPlayer(newAudioClip(getClass().getResource("/magbootsused.wav"))));

            resManager.registerMediaPlayer("atmosphereon", new SwingMediaPlayer(newAudioClip(getClass().getResource("/atmosphereon.wav"))));
            resManager.registerMediaPlayer("atmosphereoff", new SwingMediaPlayer(newAudioClip(getClass().getResource("/atmosphereoff.wav"))));
            resManager.registerMediaPlayer("coughf", new SwingMediaPlayer(newAudioClip(getClass().getResource("/coughf.wav"))));
            resManager.registerMediaPlayer("coughm", new SwingMediaPlayer(newAudioClip(getClass().getResource("/coughm.wav"))));
            resManager.registerMediaPlayer("coughdeathf", new SwingMediaPlayer(newAudioClip(getClass().getResource("/coughdeathf.wav"))));
            resManager.registerMediaPlayer("coughdeathm", new SwingMediaPlayer(newAudioClip(getClass().getResource("/coughdeathm.wav"))));

            resManager
                    .putGraphic("icon-move", SVGParsingUtils
                            .readSVG(openAsset("action-icons/move.svg")));
            resManager
                    .putGraphic("icon-turn", SVGParsingUtils
                            .readSVG(openAsset("action-icons/turn.svg")));
            resManager
                    .putGraphic("icon-pick", SVGParsingUtils
                            .readSVG(openAsset("action-icons/pick.svg")));
            resManager.putGraphic("icon-use-with", SVGParsingUtils
                    .readSVG(openAsset("action-icons/useWith.svg")));
            resManager.putGraphic("icon-use",
                    SVGParsingUtils.readSVG(openAsset("action-icons/use.svg")));
            resManager
                    .putGraphic("icon-drop", SVGParsingUtils
                            .readSVG(openAsset("action-icons/drop.svg")));

            resManager.putGraphic("icon-toggle", SVGParsingUtils
                    .readSVG(openAsset("action-icons/toggle.svg")));

            resManager.putGraphic("intro-comics0",
                    SVGParsingUtils.readSVG(openAsset("chapters/intro1.svg")));
            resManager.putGraphic("intro-comics1",
                    SVGParsingUtils.readSVG(openAsset("chapters/intro2.svg")));
            resManager.putGraphic("intro-comics2",
                    SVGParsingUtils.readSVG(openAsset("chapters/intro3.svg")));

            resManager.putGraphic("logo_github",
                    SVGParsingUtils.readSVG(openAsset("logo_github.svg")));

            resManager.putGraphic("logo_inkscape",
                    SVGParsingUtils.readSVG(openAsset("logo_inkscape.svg")));

            resManager.putGraphic("beer",
                    SVGParsingUtils.readSVG(openAsset("beer.svg")));

            for (int c = 0; c < 32; ++c) {

                resManager.putGraphic(
                        "ending" + c,
                        SVGParsingUtils.readSVG(openAsset("chapters/ending" + c
                                        + ".svg")));
            }
        } catch (IOException ex) {
            Logger.getLogger(ExploreStationApplet.class.getName()).log(Level.SEVERE, null, ex);
        }

        cmbDirection.setModel(new javax.swing.DefaultComboBoxModel(Direction.values()));
        setGameSnapshot(game);
        game.sendData("pick all");
        game.sendData("toggle atmosphere-purifier");
        game.sendData("toggle magboots");
        updateGameState();
        //playerSound.loop();
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton3 = new javax.swing.JButton();
        cmdGo = new javax.swing.JButton();
        cmbLocations = new javax.swing.JComboBox();
        cmbLoot = new javax.swing.JComboBox();
        cmdPick = new javax.swing.JButton();
        cmbInventory = new javax.swing.JComboBox();
        jSplitPane1 = new javax.swing.JSplitPane();
        pnlExploreStationView = new derelict2d.desktop.ExploreStationJPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtOutput = new javax.swing.JTextArea();
        cmbDirection = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        cmdUse = new javax.swing.JButton();
        cmdUseWith = new javax.swing.JButton();
        cmdDrop = new javax.swing.JButton();
        cmdToggle = new javax.swing.JButton();
        lblPlaceName = new javax.swing.JLabel();

        jButton3.setText("jButton1");

        cmdGo.setText("Go");
        cmdGo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdGoActionPerformed(evt);
            }
        });

        cmbLocations.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbLoot.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmdPick.setText("Pick");
        cmdPick.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPickActionPerformed(evt);
            }
        });

        cmbInventory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jSplitPane1.setResizeWeight(0.5);

        pnlExploreStationView.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                pnlExploreStationViewMouseDragged(evt);
            }
        });

        javax.swing.GroupLayout pnlExploreStationViewLayout = new javax.swing.GroupLayout(pnlExploreStationView);
        pnlExploreStationView.setLayout(pnlExploreStationViewLayout);
        pnlExploreStationViewLayout.setHorizontalGroup(
            pnlExploreStationViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 606, Short.MAX_VALUE)
        );
        pnlExploreStationViewLayout.setVerticalGroup(
            pnlExploreStationViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 599, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(pnlExploreStationView);

        txtOutput.setColumns(20);
        txtOutput.setLineWrap(true);
        txtOutput.setRows(5);
        jScrollPane1.setViewportView(txtOutput);

        jSplitPane1.setRightComponent(jScrollPane1);

        cmbDirection.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbDirection.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDirectionItemStateChanged(evt);
            }
        });

        cmdUse.setText("Use");
        cmdUse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdUseActionPerformed(evt);
            }
        });

        cmdUseWith.setText("Use with");
        cmdUseWith.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdUseWithActionPerformed(evt);
            }
        });

        cmdDrop.setText("Drop");
        cmdDrop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDropActionPerformed(evt);
            }
        });

        cmdToggle.setText("Toggle");
        cmdToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdToggleActionPerformed(evt);
            }
        });

        lblPlaceName.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblPlaceName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 905, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbLocations, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbDirection, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 6, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbInventory, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(cmdUse)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdUseWith)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdToggle)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdPick)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdDrop))
                            .addComponent(cmbLoot, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(cmdGo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblPlaceName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmbInventory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmdUse)
                            .addComponent(cmdUseWith)
                            .addComponent(cmdPick)
                            .addComponent(cmdDrop)
                            .addComponent(cmdToggle))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbLoot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbLocations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbDirection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdGo)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmdGoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdGoActionPerformed

        Direction d;
        Location l;

        game.getClient().clear();

        try {
            l = game.station.getLocation((String) cmbLocations.getSelectedItem());
            d = game.station.getAstronaut().getLocation().getConnectionDirectionForLocation(l);
            if (game.station.canMove(game.station.getAstronaut(), (String) cmbLocations.getSelectedItem())) {

                if (d != Direction.CEILING && d != Direction.FLOOR) {

                    fiveStep.play();
                } else {
                    ding.play();
                }

            }
        } catch (InvalidLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidSlotException ex) {
            Logger.getLogger(ExploreStationApplet.class.getName()).log(Level.SEVERE, null, ex);
        }

        game.sendData("move " + cmbLocations.getSelectedItem());
        this.updateGameState();
        pnlExploreStationView.repaint();
    }//GEN-LAST:event_cmdGoActionPerformed

    private void pnlExploreStationViewMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlExploreStationViewMouseDragged
        Vec2 v = new Vec2(evt.getX(), evt.getY());

        pnlExploreStationView.accScroll.x += (v.x - pnlExploreStationView.lastTouchPosition.x);
        pnlExploreStationView.accScroll.y += (v.y - pnlExploreStationView.lastTouchPosition.y);

        pnlExploreStationView.lastTouchPosition.x = (int) v.x;
        pnlExploreStationView.lastTouchPosition.y = (int) v.y;

        pnlExploreStationView.repaint();
    }//GEN-LAST:event_pnlExploreStationViewMouseDragged

    private void cmbDirectionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDirectionItemStateChanged

        game.getClient().clear();
        game.sendData("turnTo " + cmbDirection.getSelectedItem());
        System.out.println("turnTo " + cmbDirection.getSelectedItem());
        updateGameState();
    }//GEN-LAST:event_cmbDirectionItemStateChanged

    private void performAction(String actionName, String operand, String operand2) {
        game.getClient().clear();

        String line;
        line = actionName + " " + operand2 + " " + operand;
        System.out.println(">" + line);
        game.sendData(line);
        updateGameState();
    }

    private void cmdPickActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPickActionPerformed

        String operand = "";

        if (game.getCollectableItems().length > 0) {
            operand = game.getCollectableItems()[ cmbLoot.getSelectedIndex()].getName();
        }

        performAction("pick", "", operand);
    }//GEN-LAST:event_cmdPickActionPerformed

    private void cmdDropActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDropActionPerformed

        String operand = "";

        if (game.getCollectedItems().length > 0) {
            operand = game.getCollectedItems()[ cmbInventory.getSelectedIndex()].getName();
        }

        performAction("drop", operand, "");
    }//GEN-LAST:event_cmdDropActionPerformed

    private void cmdUseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdUseActionPerformed

        String operand = "";

        if (game.getCollectedItems().length > 0) {
            operand = game.getCollectedItems()[ cmbInventory.getSelectedIndex()].getName();
        }

        performAction("use", operand, "");
    }//GEN-LAST:event_cmdUseActionPerformed

    private void cmdUseWithActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdUseWithActionPerformed
        String operand2 = "";

        if (game.getCollectableItems().length > 0) {
            operand2 = game.getCollectableItems()[ cmbLoot.getSelectedIndex()].getName();
        }

        String operand = "";

        if (game.getCollectedItems().length > 0) {
            operand = game.getCollectedItems()[ cmbInventory.getSelectedIndex()].getName();
        }

        performAction("useWith", operand, operand2);
    }//GEN-LAST:event_cmdUseWithActionPerformed

    private void cmdToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdToggleActionPerformed
        String operand = "";

        if (game.getCollectedItems().length > 0) {
            operand = game.getCollectedItems()[ cmbInventory.getSelectedIndex()].getName();
        }

        performAction("toggle", operand, "");
    }//GEN-LAST:event_cmdToggleActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbDirection;
    private javax.swing.JComboBox cmbInventory;
    private javax.swing.JComboBox cmbLocations;
    private javax.swing.JComboBox cmbLoot;
    private javax.swing.JButton cmdDrop;
    private javax.swing.JButton cmdGo;
    private javax.swing.JButton cmdPick;
    private javax.swing.JButton cmdToggle;
    private javax.swing.JButton cmdUse;
    private javax.swing.JButton cmdUseWith;
    private javax.swing.JButton jButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lblPlaceName;
    private derelict2d.desktop.ExploreStationJPanel pnlExploreStationView;
    private javax.swing.JTextArea txtOutput;
    // End of variables declaration//GEN-END:variables

    void setGameSnapshot(DerelictGame derelictGame) {

        this.game = derelictGame;
    }

    void updateGameState() {

        cmbLocations.setModel(new javax.swing.DefaultComboBoxModel(game.getConnectionNames()));
        cmbLoot.setModel(new javax.swing.DefaultComboBoxModel(game.getCollectableItemNames()));
        cmbInventory.setModel(new javax.swing.DefaultComboBoxModel(game.getCollectedItemNames()));

        node = adapter.parse(game, resManager, false);
        pnlExploreStationView.setSelectedItem(node.getElementById("SVGRenderingNode_heroGraphic"));
        pnlExploreStationView.setRenderingContent(node);
        pnlExploreStationView.repaint();
        this.txtOutput.setText(game.getTextOutput());
        this.lblPlaceName.setText( game.hero.getLocation().getName() );
        if (!game.checkGameContinuityConditions()) {

            System.exit(0);
        }
    }

    @Override
    public InputStream openAsInputStream(String filename) throws IOException {
        FileInputStream fis = new FileInputStream(filename);
        return fis;
    }

    @Override
    public InputStream openAsset(String filename) throws IOException {
        System.out.println("trying for " + "/" + filename);
        return ExploreStationApplet.class.getResource("/" + filename).openStream();
    }

    @Override
    public InputStream openAsset(int resId) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OutputStream openAsOutputStream(String filename) throws IOException {

        FileOutputStream fos = new FileOutputStream(filename);
        return fos;
    }

    @Override
    public void log(String tag, String string) {
        System.out.println(tag + ":" + string);
    }
}
