# Expression Calculator

## Objective

This project implements a text-based calculator that evaluates a sequence of assignment expressions.  
It mimics a subset of Java's arithmetic expression syntax using only pure Java logic.

---

## Input Format

- Input is a series of expressions, entered line-by-line.
- To **end input**, press **Enter on an empty line**.
- Example:

a = 1\
b = a + 2\
c = 0\
++a\
c += a * b\
c++\
d = (1 + c) * 2

## Output Format

When input ends (via an empty line), the application prints out the current state of all variables, sorted alphabetically:
(a=2,b=3,c=7, d=16)

---

## Rules
- A variable name must be a **single alphabetic character** (`a` to `z`, `A` to `Z`).
- Variables are case-sensitive.
- Unassigned variables used in expressions will raise an error.
- Supported arithmetic and assignment expressions are:
    - `+` (Addition)
    - `-` (Subtraction)
    - `*` (Multiplication)
    - `=` (Assignment)
    - `+=` (Add and assign)
    - `++` (Prefix and postfix increment)
