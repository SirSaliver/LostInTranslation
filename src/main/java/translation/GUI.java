package translation;

import javax.swing.*;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            Translator translator = new JSONTranslator();
            CountryCodeConverter countryConverter =
                    new CountryCodeConverter();
            LanguageCodeConverter languageConverter =
                    new LanguageCodeConverter();

            List<String> countryNames = new ArrayList<>();

            for (String code : translator.getCountryCodes()) {
                countryNames.add(countryConverter.fromCountryCode(code));
            }

            Collections.sort(countryNames);

            JList<String> countryList = new JList<>(
                    countryNames.toArray(new String[0])
            );

            countryList.setSelectionMode(
                    ListSelectionModel.SINGLE_SELECTION
            );
            countryList.setVisibleRowCount(12);
            countryList.setSelectedValue(
                    countryConverter.fromCountryCode("can"), true
            );

            JPanel countryPanel = new JPanel(new BorderLayout(0, 8));
            countryPanel.add(new JLabel("Country:"), BorderLayout.NORTH);
            countryPanel.add(
                    new JScrollPane(countryList), BorderLayout.CENTER
            );

            List<String> languageNames = new ArrayList<>();

            for (String code : translator.getLanguageCodes()) {
                languageNames.add(languageConverter.fromLanguageCode(code));
            }

            Collections.sort(languageNames);

            JComboBox<String> languageBox = new JComboBox<>(
                    languageNames.toArray(new String[0])
            );

            languageBox.setSelectedItem(
                    languageConverter.fromLanguageCode("en")
            );

            JPanel languagePanel = new JPanel(new BorderLayout(8, 0));
            languagePanel.add(new JLabel("Language:"), BorderLayout.WEST);
            languagePanel.add(languageBox, BorderLayout.CENTER);

            JLabel resultLabel = new JLabel();

            JPanel resultPanel = new JPanel(new BorderLayout(8, 0));
            resultPanel.add(new JLabel("Translation:"), BorderLayout.WEST);
            resultPanel.add(resultLabel, BorderLayout.CENTER);

            Runnable updateTranslation = () -> {
                String countryName = countryList.getSelectedValue();
                String languageName =
                        (String) languageBox.getSelectedItem();

                if (countryName == null || languageName == null) {
                    resultLabel.setText("");
                    return;
                }

                String countryCode =
                        countryConverter.fromCountry(countryName);
                String languageCode =
                        languageConverter.fromLanguage(languageName);

                String result =
                        translator.translate(countryCode, languageCode);

                if (result == null) {
                    resultLabel.setText("No translation found!");
                } else {
                    resultLabel.setText(result);
                }
            };

            languageBox.addActionListener(event -> {
                updateTranslation.run();
            });

            countryList.addListSelectionListener(event -> {
                if (!event.getValueIsAdjusting()) {
                    updateTranslation.run();
                }
            });

            updateTranslation.run();

            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.setBorder(
                    BorderFactory.createEmptyBorder(12, 12, 12, 12)
            );

            mainPanel.add(languagePanel, BorderLayout.NORTH);
            mainPanel.add(countryPanel, BorderLayout.CENTER);
            mainPanel.add(resultPanel, BorderLayout.SOUTH);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(mainPanel);
            frame.setSize(600, 420);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}