import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

import java.awt.Font;
import java.util.EventObject;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SudokuSolverFrame extends JFrame {

	private Integer[][] puzzle = { { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 }, };
	private JPanel contentPane;
	private JTable sudokuTable;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SudokuSolverFrame frame = new SudokuSolverFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SudokuSolverFrame() {
		setTitle("Sudoku Solver by EXZACKLY");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 805);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		/*
		 * Overrides to select text on cell edit
		 */
		sudokuTable = new JTable() { 
			@Override
			public boolean editCellAt(int row, int column, EventObject e) {
				boolean result = super.editCellAt(row, column, e);
				if (getEditorComponent() == null) {
					return result;
				}
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						((JTextComponent) getEditorComponent()).selectAll();
					}
				});
				return result;
			}
		};

		sudokuTable.setRowSelectionAllowed(false);
		sudokuTable.setModel(
				new DefaultTableModel(new Object[][] {}, new String[] { "", "", "", "", "", "", "", "", "" }));
		sudokuTable.setBounds(21, 21, 700, 700);
		contentPane.add(sudokuTable);

		sudokuTable.setRowHeight(78);
		sudokuTable.setFont(new Font("Dialog", Font.PLAIN, 70));

		JLabel lblSudokuSolverBy = new JLabel("<html>Sudoku Solver <br>\r\nby EXZACKLY</html>");
		lblSudokuSolverBy.setBounds(781, 102, 133, 78);
		contentPane.add(lblSudokuSolverBy);

		JButton btnInputPuzzle = new JButton("Input Puzzle");
		btnInputPuzzle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				puzzle = parsePuzzle(JOptionPane.showInputDialog(contentPane, "Input puzzle"));
				reloadTable();
			}
		});
		btnInputPuzzle.setBounds(759, 242, 180, 43);
		contentPane.add(btnInputPuzzle);

		JButton btnSolvePuzzle = new JButton("Solve Puzzle");
		btnSolvePuzzle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				solvePuzzle(puzzle);
			}
		});
		btnSolvePuzzle.setBounds(769, 318, 170, 35);
		contentPane.add(btnSolvePuzzle);

		reloadTable();
	}

	private void reloadTable() {
		((DefaultTableModel) sudokuTable.getModel()).setRowCount(0);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		sudokuTable.setDefaultRenderer(Object.class, centerRenderer);

		for (int row = 0; row != 9; row++) {
			((DefaultTableModel) sudokuTable.getModel())
					.insertRow(row,
							new Object[] { puzzle[row][0] == 0 ? "" : puzzle[row][0],
									puzzle[row][1] == 0 ? "" : puzzle[row][1],
									puzzle[row][2] == 0 ? "" : puzzle[row][2], puzzle[row][3] == 0 ? ""
											: puzzle[row][3],
									puzzle[row][4] == 0 ? "" : puzzle[row][4],
									puzzle[row][5] == 0 ? "" : puzzle[row][5],
									puzzle[row][6] == 0 ? "" : puzzle[row][6],
									puzzle[row][7] == 0 ? "" : puzzle[row][7],
									puzzle[row][8] == 0 ? "" : puzzle[row][8] });
		}
	}

	public static Integer[][] parsePuzzle(String puzzle) {
		Integer[][] puzzleArray = new Integer[9][9];
		for (int row = 0; row != 9; row++) {
			for (int column = 0; column != 9; column++) {
				puzzleArray[row][column] = Character.getNumericValue(puzzle.charAt(0));
				puzzle = puzzle.substring(1);
			}
		}
		return puzzleArray;
	}

	public boolean solvePuzzle(Integer[][] puzzle) {
		if (puzzleIsSolved(puzzle)) {
			return true;
		}

		for (int row = 0; row != 9; row++) {
			for (int column = 0; column != 9; column++) {
				if (puzzle[row][column] == 0) {
					for (int guess = 1; guess != 10; guess++) {
						if (valueValidForCell(puzzle, guess, row, column)) {
							puzzle[row][column] = guess;
							solvePuzzle(copyArray(puzzle));
						}
					}
					return false;
				}
			}
		}
		return false;
	}

	public static Integer[][] copyArray(Integer[][] array) {
		Integer[][] returnArray = new Integer[9][9];
		for (int row = 0; row != 9; row++) {
			for (int column = 0; column != 9; column++) {
				returnArray[row][column] = array[row][column];
			}
		}
		return returnArray;
	}

	public boolean puzzleIsSolved(Integer[][] puzzle) {
		for (int row = 0; row != 9; row++) {
			for (int column = 0; column != 9; column++) {
				if (puzzle[row][column] == 0) {
					return false;
				}
			}
		}
		this.puzzle = puzzle;
		reloadTable();
		return true;
	}

	public boolean numberInRow(Integer[][] puzzle, Integer needle, Integer row) {
		for (int column = 0; column != 9; column++) {
			if (puzzle[row][column] == needle) {
				return true;
			}
		}
		return false;
	}

	public boolean numberInColumn(Integer[][] puzzle, Integer needle, Integer column) {
		for (int row = 0; row != 9; row++) {
			if (puzzle[row][column] == needle) {
				return true;
			}
		}
		return false;
	}

	public boolean numberInBox(Integer[][] puzzle, Integer needle, Integer row, Integer column) {
		for (int rowIterator = ((row) / 3) * 3; rowIterator != ((row) / 3) * 3 + 3; rowIterator++) {
			for (int columnIterator = ((column) / 3) * 3; columnIterator != ((column) / 3) * 3 + 3; columnIterator++) {
				if (puzzle[rowIterator][columnIterator] == needle) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean valueValidForCell(Integer[][] puzzle, Integer needle, Integer row, Integer column) {
		return !numberInRow(puzzle, needle, row) && !numberInColumn(puzzle, needle, column)
				&& !numberInBox(puzzle, needle, row, column);
	}
}
