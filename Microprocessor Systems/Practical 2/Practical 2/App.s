	AREA	AsmTemplate, CODE, READONLY
	IMPORT	main

; sample program makes the 4 LEDs P1.16, P1.17, P1.18, P1.19 go on and off in sequence
; (c) Mike Brady, 2011 -- 2019.

	EXPORT	start
start

IO1DIR	EQU	0xE0028018
IO1SET	EQU	0xE0028014
IO1CLR	EQU	0xE002801C
; number

	ldr r12, =0x419
	add r12, r12, #1
	ldr r11, =0x80000000
	ldr r9, =0x0
	ldr r5, =0x0

; setup registers to hold memory addresses.

	cmp r12, r11
	blo positiveNumber
	ldr r5, =0x1
	mvn r12, r12
	add r12, r12, #1
positiveNumber
	ldr r6, =0x40000100		; register that stores memory ofthe didgets to present
	ldr r7, =0x40000200		; register that stores the subtractors
	b assignSubtractors		; call subroutine that sorts out the subtractors
leaveLoop1
	b findDidgets			; call subroutine that sorts out the didgets
	add r9, r9, #1
	ldr r9, =0xe			;
	str r9, [r6]			; r6 stores the last + 1 memory address used
leaveLoop2
	b jumpATad
	
; store all the subtrators in memory.

assignSubtractors
	ldr r8, =0x1			; first subtractor
	ldr r9, =0xA			; what to multiply each new subtractor by
	ldr r10, =0x40000224	; store max memory value you will meet
whileLoop1
	cmp r7, r10				; see if less than max value
	bgt leaveLoop1			; if not, leave loop
	str r8, [r7]			; store value in memory
	add r7, r7, #4			; incriment memory spot to next word
	mul r8, r9, r8			; get next subtractor
	b whileLoop1			; goback to start of the loop

; find all the digets 

findDidgets
	ldr r8, =0x40000224		; load memory address 
	cmp r5, #0				; check if number is negative
	beq isPositive			; if not, skip next step
	ldr r9, =0xe			; load F in memory address
	str r9, [r6]			; store F in memory value to represent -
	add r6, r6, #4			; incriment memory address
isPositive
	ldr r3, =0x0			; counter for how many times the while loop should loop( 10 times )
	ldr r2, =0xA			; how many times it should loop
	ldr r4, =0x0			; register meerly holds 0
	mov r9, #0				; counter
	mov r10, #0				; boolean, started recording digets
whileLoop2
	cmp r3, r2				; see if it is still in the memory range
	bge	leaveLoop2			; leave the loop
	add r3, r3, #1			; incriement couter
	ldr r1, [r8]			; load the subtractor
whileLoop3	
	cmp r12, r1				; check if the number is less than the subtracter
	ble	leaveLoop3			; leave the loop  if so
	sub r12, r12, r1		; subtract the subtracter from the number
	add r9, r9, #1			; add 1 to the counter
	b whileLoop3
leaveLoop3
	sub r8, r8, #4			; getthe new subtrater memory address
	cmp r9, #0				; see if the counter is equal to 0
	bne jump1				; if not equal jump to a further bit of code
	cmp r10, #0				; check if a diget has been stored previusly
	beq whileLoop2			; jump back to the start of the while loop
	ldr r9, =0xf
	str r9, [r6]			; store the counter in the memory address
	ldr r9, =0x0
	add r6, r6, #4			; incriment the address for the next word
	b whileLoop2			; jump back to the start of the while loop
jump1	
	mov r10, #1				; sigmifies all subsequent "0"s are stored
	str r9, [r6]			; store the counterin the memory address
	add r6, r6, #4			; increment address to the next word
	mov r9, #0				; reset counter
	b whileLoop2			; jump back to the start of the while loop
	
	
	
jumpATad
; STEP 2

	ldr	r1,=IO1DIR
	ldr	r2,=0x000f0000		; select P1.19--P1.16
	str	r2,[r1]				; make them outputs
	ldr	r1,=IO1SET
	str	r2,[r1]				; set them to turn the LEDs off
	ldr	r2,=IO1CLR
; r1 points to the SET register
; r2 points to the CLEAR register

wloop
	ldr r5, =0x40000100		; value to start checking at
floop
	ldr r3, [r5]			; load the number thats in the memory address
	b findCode				; get the digets to send to IO1CLR
returnCode
	str	r7,[r2]	   			; clear the bit -> turn on the LED

;delay for about a half second
	ldr	r4,=30000000
dloop	
	subs	r4,r4,#1
	bne	dloop
	
	str	r7,[r1]				; set the bit -> turn off the LED
	add r5, r5, #4			; incriment to next memory address
	cmp r5, r6				; check if it is greater than the max address
	bgt wloop				; if so reset memory address
	b floop					; else just reset loop
	
	


; get code for LEDs

findCode
	cmp r3, #15				;
	bne	notZero				;
	ldr r7, =0x000f0000		;
	b returnCode
notZero
	cmp r3, #1				;
	bne	notOne				;
	ldr r7, =0x00080000		;
	b returnCode
notOne
	cmp r3, #2				;
	bne	notTwo				;
	ldr r7, =0x00040000		;
	b returnCode
notTwo
	cmp r3, #3				;
	bne	notThree			;
	ldr r7, =0x000c0000		;
	b returnCode
notThree
	cmp r3, #4				;
	bne	notFour				;
	ldr r7, =0x00020000		;
	b returnCode
notFour
	cmp r3, #5				;
	bne	notFive				;
	ldr r7, =0x000a0000		;
	b returnCode
notFive
	cmp r3, #6				;
	bne	notSix				;
	ldr r7, =0x00060000		;
	b returnCode
notSix
	cmp r3, #7				;
	bne	notSeven			;
	ldr r7, =0x000e0000		;
	b returnCode
notSeven
	cmp r3, #8				;
	bne	notEight			;
	ldr r7, =0x00010000		;
	b returnCode
notEight
	cmp r3, #9				;
	bne	notNine				;
	ldr r7, =0x00090000		;
	b returnCode
notNine
	cmp r3, 0xe
	bne notNegative
	ldr r7, =0x000d0000		;
	b returnCode			;
notNegative					;
	ldr r7, =0x0			;
	b returnCode			;
	
stop	B	stop

	END