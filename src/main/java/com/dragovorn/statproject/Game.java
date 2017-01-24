package com.dragovorn.statproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Game {

    private Menu menu;

    private List<Question> questionPool;

    private static Game instance;

    private final int MAX_HP = 5;
    private int p1;
    private int p2;
    private int winner;

    private boolean outstandingQuestion;
    private boolean correct;

    Game() {
        instance = this;

        this.menu = Menu.MAIN;
        this.p1 = this.MAX_HP;
        this.p2 = this.MAX_HP;
        this.winner = 0;
        this.outstandingQuestion = false;
        this.correct = false;
        this.questionPool = new ArrayList<>();
        initQuestions();
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 500, 500);

        if (this.menu == Menu.MAIN) {
            g.setColor(Color.WHITE);
            g.setFont(new Font(Font.DIALOG, Font.BOLD, 56));
            g.drawString("Stat Project", 100, 100);
            g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
            g.drawString("Press P to " + (this.winner != 0 ? "play again" : "start playing") + "!", 10, 200);
            g.drawString("Press R for the rules!", 10, 250);
            g.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
            g.drawString("Currently loaded " + this.questionPool.size() + " questions!", 10, 300);

            if (this.winner == 0) {
                return;
            }

            g.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
            g.drawString((this.winner == 1 ? "You Won!" : "You Lost!"), 100, 50);
        } else if (this.menu == Menu.PLAYING) {
            drawFigure(Color.RED, 30, 449, g);
            drawFigure(Color.BLUE, 385, 449, g);
            g.setColor(Color.GREEN);
            g.drawLine(0, 450, 500, 450);
            g.setColor(Color.WHITE);
            g.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
            g.drawString("You", 46, 100);
            g.drawString("Mr. Butke", 375, 100);

            for (int x = 0; x < this.p1; x++) {
                g.setColor(Color.RED);
                g.drawString("█", (30 * x), 135);
            }

            for (int x = 0; x < this.p2; x++) {
                g.setColor(Color.BLUE);
                g.drawString("█", 475 - (30 * x), 135);
            }

            if (this.outstandingQuestion) {
                g.setFont(new Font(Font.DIALOG, Font.BOLD, 25));

                if (this.correct) {
                    g.setColor(Color.WHITE);
                    g.drawString("Correct!", 190, 50);
                    playAnimation(Color.RED, 108, 200, 500, g);
                    this.p2--;

                    if (this.p2 == 0) {
                        this.menu = Menu.MAIN;
                        this.p1 = this.MAX_HP;
                        this.p2 = this.MAX_HP;
                        this.outstandingQuestion = false;
                        this.correct = false;
                        this.questionPool = new ArrayList<>();
                        this.winner = 1;
                        initQuestions();
                    }
                } else {
                    g.setColor(Color.RED);
                    g.drawString("Incorrect!", 190, 50);
                    playAnimation(Color.BLUE, 0, 200, 388, g);
                    this.p1--;

                    if (this.p1 == 0) {
                        this.menu = Menu.MAIN;
                        this.p1 = this.MAX_HP;
                        this.p2 = this.MAX_HP;
                        this.outstandingQuestion = false;
                        this.correct = false;
                        this.questionPool = new ArrayList<>();
                        this.winner = 2;
                        initQuestions();
                    }
                }

                this.outstandingQuestion = false;
            }
        } else if (this.menu == Menu.RULES) {
            g.setColor(Color.WHITE);
            g.setFont(new Font(Font.DIALOG, Font.BOLD, 60));
            g.drawString("Rules!", 100, 100);
            g.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
            g.drawString("1) Select the correct answer to the question ", 6, 200);
            g.drawString("2) Each correct answer grants a hit on your enemy, each wrong", 6, 214);
            g.drawString("answer grants your enemy a hit on you!", 6, 228);
            g.drawString("3) Defeat your enemy before they defeat you!", 6, 242);
            g.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
            g.drawString("Press 'Esc' to return to the Menu", 10, 350);
        }
    }

    public void tick() {
        if (this.menu == Menu.PLAYING) {
            if (!this.outstandingQuestion) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }

                Question question = selectQuestion();

                if (question == null) {
                    return;
                }

                ArrayList<String> options = new ArrayList<>();
                options.addAll(Arrays.asList(question.getAnswers()));
                options.add("I'll take the L");

                int answer = JOptionPane.showOptionDialog(null, question.getQuestionText(), "Question", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options.toArray(), options.toArray()[4]);

                this.correct = answer == question.getCorrect();

                this.outstandingQuestion = true;
            }
        }
    }

    public void keyPressed(KeyEvent event) {
        if (this.menu == Menu.MAIN) {
            if (event.getKeyCode() == KeyEvent.VK_P) {
                this.menu = Menu.PLAYING;
            } else if (event.getKeyCode() == KeyEvent.VK_R) {
                this.menu = Menu.RULES;
            }
        } else if (this.menu == Menu.RULES) {
            if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                this.menu = Menu.MAIN;
            }
        } else if (this.menu == Menu.PLAYING) {
            if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
        }
    }

    public static Game getInstance() {
        return instance;
    }

    private void playAnimation(Color color, int x, int y, int x2, Graphics g) {
        g.setColor(color);

        ((Graphics2D) g).setStroke(new BasicStroke(10));

        g.drawLine(x, y, x2, y);
    }

    private void drawFigure(Color color, int x, int y, Graphics g) {
        g.setColor(color);

        ((Graphics2D) g).setStroke(new BasicStroke(8));

        g.drawOval(x, y - 300, 80, 80);
        g.drawLine(x + 40, y - 220, x + 40, y - 100);
        g.drawLine(x + 40, y - 100, x, y);
        g.drawLine(x + 40, y - 100, x + 90, y);
        g.drawLine(x + 40, y - 200, x, y - 120);
        g.drawLine(x + 40, y - 200, x + 90, y - 120);
    }

    private Question selectQuestion() {
        if (this.questionPool.size() == 0) {
            return null;
        }

        Question question = this.questionPool.get(ThreadLocalRandom.current().nextInt(0, this.questionPool.size()));

        this.questionPool.remove(question);

        return question;
    }

    private void initQuestions() {
        this.questionPool.add(new Question("Let X be a normal Random Variable with a mean of 100 and a standard deviation of 50.\nWhat is the probability that a randomly selected value is between 0 and 150?", "82.5", "82", "81.5", "81", 2));
        this.questionPool.add(new Question("Here are annual numbers of death from tornadoes in the US from 1990-2000\n\n53   39   39   33   69   30   25   67   130   94   40\nWhat is the mean?", "56", "94", "63.9", "56.3", 3));
        this.questionPool.add(new Question("Given a data set of 1, 2, 3, 4, and 5.\n List the IQR, Standard Deviation, and Variance, of the data set.", "IQR=0, Standard Deviation=1.58, Variance=2", "IQR=2, Standard Deviation=0, Variance=1.58", "IQR=1.58, Standard Deviation=2, Variance=0", "IQR=2, Standard Deviation=1.58, Variance=0", 3));
        this.questionPool.add(new Question("Here are annual numbers of death from tornadoes in the US from 1990-2000\n\n53   39   39   33   69   30   25   67   130   94   40\nWhat is the median?", "25", "69", "40", "40.5", 2));
        this.questionPool.add(new Question("Here are annual numbers of death from tornadoes in the US from 1990-2000\n\n53   39   39   33   69   30   25   67   130   94   40\nWhat is Q1 and Q3?", "33, 69", "25, 130", "36, 68", "39.5, 46.5", 0));
        this.questionPool.add(new Question("Here are annual numbers of death from tornadoes in the US from 1990-2000\n\n53   39   39   33   69   30   25   67   130   94   40\nAre there any outliers?", "None", "1 upper outlier, no lower outlier", "no upper outlier, 1 lower outlier", "1 upper outlier, 1 lower outlier", 0));
        this.questionPool.add(new Question("Here are annual numbers of death from tornadoes in the US from 1990-2000\n\n53   39   39   33   69   30   25   67   130   94   40\nWould this data be quantitative or categorical?", "Quantitative", "Categorical", "", "", 0));
        this.questionPool.add(new Question("Here are annual numbers of death from tornadoes in the US from 1990-2000\n\n53   39   39   33   69   30   25   67   130   94   40\nWhat would the shape of the box-and-whisker plot be?", "Left skew", "Right skew", "Symmetrical", "Neutral", 1));
        this.questionPool.add(new Question("What type of graph does a stem and leaf plot represent when turned vertically?", "Pie chart", "Stem and leaf plot", "Box plot", "Histogram", 3));
        this.questionPool.add(new Question("What can stems not represent?", "Ones", "Tens", "Thousands", "Ten thousands", 0));
        this.questionPool.add(new Question("For a scatter plot with a strong upward slope, it would have a correlation coefficient closer to:", "-0.3", "-0.9", "+0.7", "+0.6", 2));
        this.questionPool.add(new Question("What is a segmented bar graph best used for?", "To present data as percents on a circular graph", "To compare data as a whole", "To display the distribution of data based on the 5 number summary", "To compare different classes to the whole", 1));
        this.questionPool.add(new Question("If it only rains on mondays, what is the probability that it will rain in a two week period?", "7/14", "2/14", "12/14", "1/14", 1));
        this.questionPool.add(new Question("What is the method of modeling chance behavior that accurately mimics the situation being considered?", "Experiment", "Census", "Simulation", "Simple Random Sample", 2));
        this.questionPool.add(new Question("When two events cannot happen at the same time this is called ______", "Disjointed events", "Independent events", "The last two answers", "None of these", 0));
        this.questionPool.add(new Question("Why would you use the LLN (law of large numbers) theorem when doing an experiment?", "To make sure that you have the least amount of variation in your results of your experiment.", "So you have a big experiment.", "To make your experiment look official.", "", 0));
        this.questionPool.add(new Question("The probability of A given that B has occurred is called ________", "Conditional probability", "Simple random sample", "Conditional distribution", "Probability distribution", 0));
        this.questionPool.add(new Question("Let Z be a standard normal random variable.\nFind z* such that the probability that a randomly selected value of z is greater then z is .35", "-.39", ".39", "-.56", ".56", 0));
        this.questionPool.add(new Question("What is a segmented bar graph best used for?", "To present data as percents on a circular graph", "To compare data as a whole", "To display the distribution of data based on the 5 number summary", "To compare different classes to the whole", 1));
        this.questionPool.add(new Question("A survey is placed into a school newspaper and students are able to complete and return it to a dropbox in the main office.\n What type of sampling method is used?", "Convenience sample", "Random sample", "Voluntary response sample", "Multistage sample", 2));
        this.questionPool.add(new Question("Your friend wants to survey 100 people at his local mall. He goes around asking every 8th person he sees.\n What type of sample is this?", "Systematic random sample", "Cluster sample", "Stratified random sample", "Simple random sample", 0));
        this.questionPool.add(new Question("A study that is based on data in which no manipulation of factors have been employed is an example of a _________", "Retrospective study", "Prospective study", "Observational study", "", 2));
        this.questionPool.add(new Question("Blocking is used in ______ and stratification is used in ______", "Observation studies, experiments", "Experiments, sampling", "Sampling, experiments", "Experiments, prospective studies", 1));
    }
}