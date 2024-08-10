use std::marker::PhantomData;

#[repr(C)]
pub struct JavaString<'a> {
    ptr: *const u8,
    len: usize,
    _pd: PhantomData<&'a ()>,
}

impl<'a> From<&'a str> for JavaString<'a> {
    fn from(value: &'a str) -> Self {
        Self {
            ptr: value.as_ptr(),
            len: value.len(),
            _pd: PhantomData,
        }
    }
}

pub struct Helper;

impl Helper {
    pub fn say<'a>(str: impl Into<crate::JavaString<'a>>) {
        unsafe {
            crate::say(str.into());
        }
    }

    pub fn add<'a>(a: i32, b: i32) {
        unsafe {
            crate::add(a, b);
        }
    }
}

#[link(wasm_import_module = "nz.laspruca.Helper")]
extern "C" {
    fn say(str: crate::JavaString);
    fn add(num1: i32, num2: i32);
}

#[no_mangle]
#[inline(never)]
pub extern "C" fn hello() {
    Helper::say("Hello world");
}

#[no_mangle]
pub extern "C" fn yolo() {
    hello();
    Helper::add(1, 2);
}
