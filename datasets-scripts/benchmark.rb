require 'pry'

compress_file = ['ukbench_data.zip','ukbench.zip']

files = `ls`.split("\n")

if !(compress_file - files).empty?
	puts "missing files: #{compress_file}"
	exit
end

files_to_delete = files.select do |file|
	!file.end_with?('.rb') && !compress_file.include?(file)
end

`rm #{files_to_delete.join(' ')} -rvf`

compress_file.map do |file|
	puts "Unzip #{file} start"
	`unzip #{file}`
	puts "Unzip #{file} end"
end

files = `ls`.scan(/overview\d+.html/)

all_size = 0

files.map do |overview_file|
	content_file = File.read(overview_file)
	images = content_file.scan(/href=(.+?)>/).flatten
	set_id = overview_file.scan(/\d+/).first.to_i
	set_size = images.size
	folder_name = "ukbench-#{set_id.to_s.rjust(2, "0")}-#{set_size}"

	`mkdir #{folder_name}`
	images.map do |file|
		puts "set #{set_id} moving #{file}"
		`cp #{file} #{folder_name}/`
	end
	all_size += set_size
end
`mv full ukbench-#{all_size}`

files_to_delete = `ls`.split("\n").select do |file|
	!file.end_with?('.rb') && !compress_file.include?(file) && !file.start_with?('ukbench-')
end

`rm #{files_to_delete.join(' ')} -rvf`