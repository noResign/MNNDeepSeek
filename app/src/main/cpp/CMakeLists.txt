cmake_minimum_required(VERSION 3.10.2)

project("mnn_deep_seek")

aux_source_directory(./ SRC_LIST)

add_library(mnn_deep_seek SHARED ${SRC_LIST})

find_library(log-lib log)
find_library(android-lib android)

include_directories(${CMAKE_SOURCE_DIR}/mnn/include)
include_directories(${CMAKE_SOURCE_DIR}/mnn/include/expr/)
add_library(libMNN STATIC IMPORTED)
add_library(libMNN_CL STATIC IMPORTED)
add_library(libMNN_Express STATIC IMPORTED)
add_library(libllm STATIC IMPORTED)
set_target_properties(
        libMNN PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/mnn/${CMAKE_ANDROID_ARCH_ABI}/libMNN.so
)
set_target_properties(
        libMNN_CL PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/mnn/${CMAKE_ANDROID_ARCH_ABI}/libMNN_CL.so
)
set_target_properties(
        libMNN_Express PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/mnn/${CMAKE_ANDROID_ARCH_ABI}/libMNN_Express.so
)
set_target_properties(
        libllm PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/mnn/${CMAKE_ANDROID_ARCH_ABI}/libllm.so
)

message("===>>>> abi is : ${CMAKE_ANDROID_ARCH_ABI} <<<<===")

target_link_libraries(
        mnn_deep_seek

        ${log-lib}
        ${android-lib}
        libMNN
        libMNN_CL
        libMNN_Express
        libllm
)