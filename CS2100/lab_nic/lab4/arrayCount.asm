# arrayCount.asm
  .data 
arrayA: .word 1, 0, 2, 0, 3, 48, 22, 16   # arrayA has 8 values
count:  .word 0             # dummy value

  .text
main:
    # code to setup the variable mappings
    la $t0, arrayA		# t0 = ptr to arrayA
	la $t7, count		# t8 = count
	lw $t8, 0($t7)

    # code for reading in the user value X
	li $v0, 5			# system call code for read_int
	syscall				# read the integer
	addi $t1, $v0, 0	# store integer

    # code for counting multiples of X in arrayA
	addi $t5, $t1, -1	# Make mask
	addi $t6, $zero, 8	# break point for loop
loop: 
    lw $t2, 0($t0)		# num in arrayA
	beq $t6, $zero, end # break if end
	and $t3, $t2, $t5	# use mask
	bne $t3, $zero, skip # skip if not divisible
	addi $t8, $t8, 1	# count++
skip:
	addi $t0, $t0, 4	# ptr++
	addi $t6, $t6, -1	# loop--
	j	loop
end:
	# store back count
	sw $t8, 0($t7)

	# code for printing result
	li $v0, 1			# system call code for print_int
	addi $a0, $t8, 0	# load count to print
	syscall				# print count
	
    # code for terminating program
    li  $v0, 10
    syscall
