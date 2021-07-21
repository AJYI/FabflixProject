/*
 * GccApplication4.c
 *
 * Created: 6/9/2021 1:57:15 PM
 * Author : Alex Yi
 */

#include <avr/io.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include "avr.h"
#include "lcd.h"
#include "string.h"

// Function Prototype
int get_key();
int is_pressed();
void print_title_screen();
int debouncer();
void confirmationScreen(int num);
void init_game1();
void init_game2();
void print_title_prompt(char *topPart, char *bottomPart);
void create_memory_game(int difficulty);
void createGameTile(char *gameTiles, int choices);

// our randomValue for the randomness part
int randValue = 0;

// Getting the key from the keypad
int get_key()
{
    int i, j;
    for (i = 0; i < 4; i++)
    {
        // We use these values for randomness later on
        randValue += i;
        for (j = 0; j < 4; j++)
        {
            // We use these values for randomness later on
            randValue += j;
            if (is_pressed(i, j))
            {
                avr_wait(200);
                return ((i * 4) + j + 1);
            }
        }
    }
    // To generate more randomness
    double logValue = log(randValue) * 10000;
    randValue += (int)logValue;
    return 0;
}

// Checking if the key is pressed
int is_pressed(int r, int c)
{
    // Setting all GPIO to N/C
    DDRC = 0;
    PORTC = 0;

    // Setting r to "0"
    SET_BIT(DDRC, r);  // First we set the bit to 1
    CLR_BIT(PORTC, r); // Then we clear_bit PORTC to get to ground

    // Setting c + 4 to "w1"
    CLR_BIT(DDRC, c + 4);  // We clear the bit to go to the 0 on the tree
    SET_BIT(PORTC, c + 4); // Then we set bit to 1 to get to the "weak1"

    avr_wait(10);

    if (GET_BIT(PINC, c + 4))
    {
        return 0;
    }
    return 1;
}

void print_title_screen()
{
    char buff[17];

    // Game 1
    lcd_pos(0, 0);
    sprintf(buff, "Memory Game");
    lcd_puts(buff);

    // Game 2
    lcd_pos(1, 0);
    sprintf(buff, "Press 1 to start");
    lcd_puts(buff);
}

int debouncer()
{
    int k;
    while (1)
    {
        while ((k = get_key()) == 0)
            ;

        // implementations in between

        while (get_key() != 0)
            ;
        break;
    }
    return k;
}

void confirmationScreen(int num)
{
    char buff[17];

    lcd_pos(0, 0);
    sprintf(buff, "Game %d selected?", num);
    lcd_puts(buff);

    lcd_pos(1, 0);
    sprintf(buff, "1 = YES | 2 = NO");
    lcd_puts(buff);
}

// Our driver class for the menu
int main(void)
{
    // We first initialize the LCD
    lcd_init();
    print_title_screen();

    while (1)
    {
        int k = debouncer();

        if (k == 1)
        {
            confirmationScreen(1);
            // To either select 1 or 2
            while (1)
            {
                int j = debouncer();
                // For yes
                if (j == 1)
                {
                    init_game1();
                }
                // For no
                if (j == 2)
                {
                    lcd_clr();
                    print_title_screen();
                    break;
                }
            }
        }
        // else if(k == 2){
        // 	confirmationScreen(2);
        // 	// To either select 1 or 2
        // 	while(1){
        // 		int j = debouncer();
        // 		// For yes
        // 		if(j == 1){
        // 			//init_game2();
        // 		}
        // 		// For no
        // 		if(j == 2){
        // 			lcd_clr();
        // 			print_title_screen();
        // 			break;
        // 		}
        // 	}
        // }
    }
}

void print_title_prompt(char *topPart, char *bottomPart)
{
    lcd_clr();
    char buff[17];
    lcd_pos(0, 0);
    sprintf(buff, "%s", topPart);
    lcd_puts(buff);
    lcd_pos(1, 0);
    sprintf(buff, "%s", bottomPart);
    lcd_puts(buff);
    avr_wait(2000);
    lcd_clr();
    return;
}

//################
//#  Memory game #
//################
void init_game1()
{

    // Game title
    char topPart[17] = "Memory Game";
    char bottomPart[17] = "Test your memory";
    print_title_prompt(topPart, bottomPart);

    // Menu
    char buff[17];
    lcd_pos(0, 0);
    sprintf(buff, "  Select Level");
    lcd_puts(buff);
    lcd_pos(1, 0);
    sprintf(buff, "  1   2   3   4");
    lcd_puts(buff);

    while (1)
    {
        int k = debouncer();
        if (k == 1)
        {
            create_memory_game(1);
        }
        else if (k == 2)
        {
            create_memory_game(2);
        }
        else if (k == 3)
        {
            create_memory_game(3);
        }
        else if (k == 4)
        {
            create_memory_game(4);
        }
    }
}

void createGameTile(char *gameTiles, int choices)
{
    // difficulty 1 = ABC
    // difficulty 2 = ABCD
    // difficulty 3 = ABCDE
    // difficulty 4 = ABCDEF

    // This portion just gets the substring that we will use depending on the difficulty
    char letter_choices[7] = "ABCDEF";
    int total = choices / 2;
    char actual_choices[total + 1]; //Ensures the null terminator
    memcpy(actual_choices, &letter_choices[0], total);
    actual_choices[total] = '\0';

    //Now we want to randomize the string
    char gameTilesObj[choices + 1];

    // Creating the game tiles obj
    gameTilesObj[0] = '\0';
    strncat(gameTilesObj, actual_choices, total);
    strncat(gameTilesObj, actual_choices, total);
    gameTilesObj[choices] = '\0';
    // Now we will randomize the array

    int i;

    // our random seed generator
    // we want to ensure that our rand value never goes over 2^31bits
    randValue = randValue % 123456789;
    srand(randValue);

    for (i = 0; i < choices; i++)
    {
        int swapValue = rand() % choices;

        // We swap the choices
        char temp = gameTilesObj[swapValue];
        gameTilesObj[swapValue] = gameTilesObj[i];
        gameTilesObj[i] = temp;
    }

    // final string to return
    strcpy(gameTiles, gameTilesObj);
}

void create_memory_game(int difficulty)
{
    // Game tiles and the amount of choices depending on the difficulty
    int choices = 4 + (2 * difficulty);

    // our initializing values
    char gameTiles[17]; // The solution
    char chances[4] = "OOO";
    char gameArray[17]; // The game board
    int chancesAmount = 3;

    // Generating the game board solution
    lcd_clr();
    createGameTile(gameTiles, choices);

    char buff[17];

    // Top part of the screen
    lcd_pos(0, 0);
    sprintf(buff, "Remember in 3s");
    lcd_puts(buff);

    lcd_pos(1, 0);
    sprintf(buff, "%s", gameTiles);
    lcd_puts(buff);

    // After three second is over
    avr_wait(3000);

    lcd_clr();

    // Creates the upper part of the screen prompt
    lcd_pos(0, 0);
    sprintf(buff, "Chances: %s", chances);
    lcd_puts(buff);

    // Creates the bottom part of the screen prompt
    int i;
    for (i = 0; i < choices; i++)
    {
        gameArray[i] = 'X';
    }
    gameArray[choices] = '\0';
    lcd_pos(1, 0);
    lcd_puts(gameArray);

    //%%%%%%%%%%%%%%%%%%%%%%%
    // The actual game starts
    //%%%%%%%%%%%%%%%%%%%%%%%

    int firstChoice = 0;
    int firstChoiceIndex = -1;
    int secondChoice = 0;
    int secondChoiceIndex = -1;

    while (1)
    {
        // The case when we have uncovered everything
        if (strcmp(gameTiles, gameArray) == 0)
        {
            // Just to make sure the user knows what they inputted
            avr_wait(750);
            lcd_clr();
            lcd_pos(0, 0);
            sprintf(buff, "WINNER!");
            lcd_puts(buff);
            avr_wait(3000);
            break;
        }

        // If both choices are chosen
        if (firstChoice == 1 && secondChoice == 1)
        {
            // Just to make sure the user knows what they inputted
            avr_wait(750);

            // If we get a match
            if (gameTiles[firstChoiceIndex] == gameTiles[secondChoiceIndex])
            {
                // We reset the value
                firstChoice = 0;
                firstChoiceIndex = -1;
                secondChoice = 0;
                secondChoiceIndex = -1;

                // Prompts the user a match has been made
                lcd_clr();
                lcd_pos(0, 0);
                sprintf(buff, "Made a match!");
                lcd_puts(buff);

                avr_wait(1000);

                // Then resets the screen back to what it was
                lcd_clr();
                lcd_pos(0, 0);
                sprintf(buff, "Chances: %s", chances);
                lcd_puts(buff);

                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }

            // If the choices aren't a match
            if (gameTiles[firstChoiceIndex] != gameTiles[secondChoiceIndex])
            {

                // We increment the chances
                chancesAmount--;
                int j;
                int upperValue = 3 - chancesAmount;
                for (j = 0; j < upperValue; j++)
                {
                    chances[j] = 'X';
                }

                // We reset the value
                gameArray[firstChoiceIndex] = 'X';
                gameArray[secondChoiceIndex] = 'X';

                firstChoice = 0;
                firstChoiceIndex = -1;
                secondChoice = 0;
                secondChoiceIndex = -1;

                // Prompts the user a mismatch has been made
                lcd_clr();
                lcd_pos(0, 0);
                sprintf(buff, "Mismatch!");
                lcd_puts(buff);

                // Then resets the screen back to what it was
                avr_wait(1000);

                // Then resets the screen back to what it was
                lcd_clr();
                lcd_pos(0, 0);
                sprintf(buff, "Chances: %s", chances);
                lcd_puts(buff);

                lcd_pos(1, 0);
                lcd_puts(gameArray);

                // First checks if all the chances are gone
                if (chancesAmount == 0)
                {
                    lcd_clr();
                    lcd_pos(0, 0);
                    sprintf(buff, "YOU LOSE");
                    lcd_puts(buff);
                    avr_wait(3000);
                    break;
                }
            }
        }

        int k = debouncer();
        //Reset
        if (k == 16)
        {
            init_game1();
        }
        if (k == 0)
        {
            continue;
        }
        else if (k != 0)
        {
            k--;
        }

        // We need to handle if the user chooses the same choice
        if (k == firstChoiceIndex || k == secondChoiceIndex)
        {
            continue;
        }

        if (gameArray[k] != 'X')
        {
            continue;
        }

        // We need logic for the 12 choices
        if (k == 0 && k < choices)
        {
            if (firstChoice == 0)
            {
                firstChoiceIndex = k;
                firstChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
            else if (secondChoice == 0)
            {
                secondChoiceIndex = k;
                secondChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
        }
        if (k == 1 && k < choices)
        {
            if (firstChoice == 0)
            {
                firstChoiceIndex = k;
                firstChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
            else if (secondChoice == 0)
            {
                secondChoiceIndex = k;
                secondChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
        }
        if (k == 2 && k < choices)
        {
            if (firstChoice == 0)
            {
                firstChoiceIndex = k;
                firstChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
            else if (secondChoice == 0)
            {
                secondChoiceIndex = k;
                secondChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
        }
        if (k == 3 && k < choices)
        {
            if (firstChoice == 0)
            {
                firstChoiceIndex = k;
                firstChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
            else if (secondChoice == 0)
            {
                secondChoiceIndex = k;
                secondChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
        }
        if (k == 4 && k < choices)
        {
            if (firstChoice == 0)
            {
                firstChoiceIndex = k;
                firstChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
            else if (secondChoice == 0)
            {
                secondChoiceIndex = k;
                secondChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
        }
        if (k == 5 && k < choices)
        {
            if (firstChoice == 0)
            {
                firstChoiceIndex = k;
                firstChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
            else if (secondChoice == 0)
            {
                secondChoiceIndex = k;
                secondChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
        }
        if (k == 6 && k < choices)
        {
            if (firstChoice == 0)
            {
                firstChoiceIndex = k;
                firstChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
            else if (secondChoice == 0)
            {
                secondChoiceIndex = k;
                secondChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
        }
        if (k == 7 && k < choices)
        {
            if (firstChoice == 0)
            {
                firstChoiceIndex = k;
                firstChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
            else if (secondChoice == 0)
            {
                secondChoiceIndex = k;
                secondChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
        }
        if (k == 8 && k < choices)
        {
            if (firstChoice == 0)
            {
                firstChoiceIndex = k;
                firstChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
            else if (secondChoice == 0)
            {
                secondChoiceIndex = k;
                secondChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
        }
        if (k == 9 && k < choices)
        {
            if (firstChoice == 0)
            {
                firstChoiceIndex = k;
                firstChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
            else if (secondChoice == 0)
            {
                secondChoiceIndex = k;
                secondChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
        }
        if (k == 10 && k < choices)
        {
            if (firstChoice == 0)
            {
                firstChoiceIndex = k;
                firstChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
            else if (secondChoice == 0)
            {
                secondChoiceIndex = k;
                secondChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
        }
        if (k == 11 && k < choices)
        {
            if (firstChoice == 0)
            {
                firstChoiceIndex = k;
                firstChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
            else if (secondChoice == 0)
            {
                secondChoiceIndex = k;
                secondChoice = 1;
                gameArray[k] = gameTiles[k];
                lcd_pos(1, 0);
                lcd_puts(gameArray);
            }
        }
        // More implementation here
    }

    // After game is finished we go back to the title screen
    init_game1();
}